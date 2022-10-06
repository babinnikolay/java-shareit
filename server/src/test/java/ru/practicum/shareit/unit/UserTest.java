package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private UserService userService;
    @Mock
    private UserStorage userStorageStub;
    private UserDto userDto;
    private Long userId;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorageStub);

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@email.email");

        userId = 1L;

        user = new User();
    }

    @Test
    void whenDeleteNonExistsUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.deleteUserById(1L));
    }

    @Test
    void whenGetAllThenVerifyCallFindAllMethod() {
        userService.getAllUsers();
        verify(userStorageStub, times(1)).findAll();
    }

    @Test
    void whenGetNonExistsUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void whenUpdateNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUser(userId, userDto));
    }

    @Test
    void whenUpdateUserWithoutEmailThenThrowConflictException() {
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(userStorageStub.findByEmail(userDto.getEmail())).thenReturn(Optional.of(newUser));
        assertThrows(ConflictException.class,
                () -> userService.updateUser(userId, userDto));
    }

}