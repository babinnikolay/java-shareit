package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentStorage;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetItemTest {

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
    private Long itemId;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemStorageStub, userStorageStub,
                bookingStorageStub, commentStorageStub, itemRequestStorageStub);
        userId = 1L;
        itemId = 1L;
    }

    @Test
    void whenGetItemNonExistItemThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.getItem(itemId, userId));
    }
}