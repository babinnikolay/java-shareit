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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateItemTest {

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
    private ItemDto itemDto;
    private Long userId;
    private User user;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemStorageStub, userStorageStub,
                bookingStorageStub, commentStorageStub, itemRequestStorageStub);
        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(false);
        userId = 1L;
        user = new User();
    }

    @Test
    void whenCreateItemNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoWithoutNameThenThrowValidateException() {
        itemDto.setName("");
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(ConstraintViolationException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoWithoutDescriptionThenThrowValidateException() {
        itemDto.setDescription("");
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(ConstraintViolationException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoWithoutAvailableThenThrowValidateException() {
        itemDto.setAvailable(null);
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(ConstraintViolationException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoWithNonExistsRequestThenThrowNotFoundException() {
        itemDto.setRequestId(1L);
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoSaveThenOk() throws NotFoundException {
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        itemDto = itemService.createItem(itemDto, userId);
        assertNotNull(itemDto);
    }

}