package ru.practicum.shareit.unit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.item.CommentStorage;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.UserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SearchItemsTest {

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

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemStorageStub, userStorageStub,
                bookingStorageStub, commentStorageStub, itemRequestStorageStub);
    }

    @SneakyThrows
    @Test
    void whenSearchBlackTextThenThrowNotFoundException() {
        assertEquals(0, itemService.searchItems("", 0, 1).size());
    }
}