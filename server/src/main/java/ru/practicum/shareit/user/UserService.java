package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto) throws NotFoundException, ConflictException;

    UserDto getUserById(Long userId) throws NotFoundException;

    void deleteUserById(Long userId) throws NotFoundException;

    Collection<UserDto> getAllUsers();
}
