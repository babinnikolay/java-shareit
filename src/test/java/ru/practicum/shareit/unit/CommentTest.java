package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentStorage;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentTest {

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
    private User user;
    private Long itemId;
    private Item item;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemStorageStub, userStorageStub,
                bookingStorageStub, commentStorageStub, itemRequestStorageStub);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(false);
        userId = 1L;
        user = new User();
        itemId = 1L;
        item = new Item();
        commentDto = new CommentDto();
        commentDto.setText("text");
    }

    @Test
    void whenCreateCommentWithoutTestThrowValidateException() {
        commentDto.setText("");
        assertThrows(ConstraintViolationException.class,
                () -> itemService.createComment(itemId, commentDto, userId));
    }

    @Test
    void whenCreateCommentNonExistsAuthorThrowNonFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.createComment(itemId, commentDto, userId));
    }

    @Test
    void whenCreateCommentWithoutBookingsThenThrowBadRequestException() {
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(itemStorageStub.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingStorageStub.findBookingByItemIdAndStatusAndStartBefore(any(), any(), any()))
                .thenReturn(Collections.emptySet());
        assertThrows(BadRequestException.class,
                () -> itemService.createComment(itemId, commentDto, userId));
    }
}