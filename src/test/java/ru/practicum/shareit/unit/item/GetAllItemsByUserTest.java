package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentStorage;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetAllItemsByUserTest {

    private ItemService itemService;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemStorage itemStorageStub;
    @Mock
    private BookingStorage bookingStorageStub;
    @Mock
    private CommentStorage commentStorageStub;
    @Mock
    private ItemRequestStorage itemRequestStorageStub;
    private Long userId;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemStorageStub, userStorageStub,
                bookingStorageStub, commentStorageStub, itemRequestStorageStub);
        userId = 1L;
    }

    @Test
    void whenGetAllItemsNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsByUser(userId, 0, 1));
    }
}