package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.impl.UserServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetAllUsersTest {

    private UserService userService;
    @Mock
    private UserStorage userStorageStub;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorageStub);
    }

    @Test
    void whenGetAllThenVerifyCallFindAllMethod() {
        userService.getAllUsers();
        verify(userStorageStub, times(1)).findAll();
    }
}