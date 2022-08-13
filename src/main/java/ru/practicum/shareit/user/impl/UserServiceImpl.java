package ru.practicum.shareit.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserAlreadyExists;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) throws UserAlreadyExists {
        checkUserByEmail(userDto);
        validateUser(userDto);
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return UserMapper.toUserDto(userStorage.saveUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserAlreadyExists {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("User %s not found", userDto.getEmail()));
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            checkUserByEmail(userDto);
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userStorage.saveUser(user));
    }

    @Override
    public UserDto getUserById(Long userId) throws UserNotFoundException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with id %d not found", userId));
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundException {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("User with id %d not found", userId));
        }
        userStorage.deleteUserById(userId);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void validateUser(UserDto userDto) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void checkUserByEmail(UserDto userDto) throws UserAlreadyExists {
        User user = userStorage.getUserByEmail(userDto.getEmail());
        if (user != null) {
            throw new UserAlreadyExists(String.format("User with email %s already exist",
                    userDto.getEmail()));
        }
    }
}
