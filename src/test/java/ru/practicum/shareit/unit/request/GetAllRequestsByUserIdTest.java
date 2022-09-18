package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.requests.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetAllRequestsByUserIdTest {

    private ItemRequestService itemRequestService;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemRequestStorage itemRequestStorageStub;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestStorageStub, userStorageStub);
    }

    @Test
    void whenGetAllRequestsNonExistsUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllRequestsByUserId(1L));
    }
}