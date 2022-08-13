package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException, UserAlreadyExistsException;

    UserDto getUserById(Long userId) throws UserNotFoundException;

    void deleteUserById(Long userId) throws UserNotFoundException;

    Collection<UserDto> getAllUsers();
}
