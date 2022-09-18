package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetUserByIdTest {

    private UserService userService;
    @Mock
    private UserStorage userStorageStub;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorageStub);
    }

    @Test
    void whenGetNonExistsUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.getUserById(1L));
    }
}