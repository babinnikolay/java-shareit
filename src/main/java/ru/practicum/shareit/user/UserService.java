package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserAlreadyExists;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto) throws UserAlreadyExists;

    UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserAlreadyExists;

    UserDto getUserById(Long userId) throws UserNotFoundException;

    void deleteUserById(Long userId) throws UserNotFoundException;

    Collection<UserDto> getAllUsers();
}
