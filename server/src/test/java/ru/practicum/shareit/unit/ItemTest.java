package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemTest {

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
    private Long itemId;
    private Item item;
    private User owner;

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
        itemId = 1L;
        item = new Item();
        item.setId(1L);
        owner = new User();
    }

    @Test
    void whenCreateItemNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.createItem(itemDto, userId));
    }

    @Test
    void whenItemDtoWithDescriptionThenOk() throws NotFoundException {
        itemDto.setDescription("desc");
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        ItemDto newItem = itemService.createItem(itemDto, userId);
        assertEquals("desc", newItem.getDescription());
    }

    @Test
    void whenItemDtoWithAvailableThenOk() throws NotFoundException {
        itemDto.setAvailable(true);
        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        ItemDto newItem = itemService.createItem(itemDto, userId);
        assertEquals(true, newItem.getAvailable());
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

    @Test
    void whenGetAllItemsNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsByUser(userId, 0, 1));
    }

    @Test
    void whenGetItemNonExistItemThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.getItem(itemId, userId));
    }

    @Test
    void whenSearchBlackTextThenThrowNotFoundException() throws NotFoundException {
        assertEquals(0, itemService.searchItems("", 0, 1).size());
    }

    @Test
    void whenUpdateNonExistItemThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemDto, itemId, userId));
    }

    @Test
    void whenUpdateItemNonOwnerUserThenThrowNotFoundException() {
        item.setOwner(owner);
        owner.setId(2L);

        when(itemStorageStub.findById(itemId)).thenReturn(Optional.of(item));
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemDto, itemId, userId));
        assertEquals("User with id 1 is not owner", e.getMessage());
    }

    @Test
    void whenUpdateItemThenOk() throws NotFoundException {
        item.setOwner(owner);
        owner.setId(1L);

        itemDto.setName("newName");
        itemDto.setDescription("newDesc");
        itemDto.setAvailable(true);

        when(itemStorageStub.findById(itemId)).thenReturn(Optional.of(item));
        itemService.updateItem(itemDto, itemId, userId);

        assertEquals("newName", item.getName());
        assertEquals("newDesc", item.getDescription());
        assertTrue(item.isAvailable());
    }
}