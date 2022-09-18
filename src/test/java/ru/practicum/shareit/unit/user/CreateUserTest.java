package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    private UserService userService;
    @Mock
    private UserStorage userStorageStub;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorageStub);

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@email.email");
    }

    @Test
    void whenCreateUserWithoutNameThenThrowValidateException() {
        userDto.setName(null);
        assertThrows(ConstraintViolationException.class,
                () -> userService.createUser(userDto));
    }

    @Test
    void whenCreateUserWithoutEmailThenThrowValidateException() {
        userDto.setEmail(null);
        assertThrows(ConstraintViolationException.class,
                () -> userService.createUser(userDto));
    }

    @Test
    void whenCreateUserWithWrongEmailThenThrowValidateException() {
        userDto.setEmail("NotAnEmail.com");
        assertThrows(ConstraintViolationException.class,
                () -> userService.createUser(userDto));
    }
}