package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestTest {

    private ItemRequestService itemRequestService;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemRequestStorage itemRequestStorageStub;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestStorageStub, userStorageStub);
        itemRequestDto = new ItemRequestDto();
    }

    @Test
    void whenCreateRequestNonExistUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.createRequest(itemRequestDto, 1L));
    }

    @Test
    void whenGetAllRequestsNonExistsUserThenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllRequestsByUserId(1L));
    }

    @Test
    void whenGetAllRequestsNonExistUserThenThrowNotFoundException() {
        Long ownerId = 1L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllRequests(ownerId, 0, 1));
    }

    @Test
    void whenGetItemRequestNonExistsUserThenThrowNotFoundException() {
        Long ownerId = 1L;
        Long itemId = 1L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequest(ownerId, itemId));
    }

    @Test
    void whenGetItemRequestNonExistsItemRequestThenThrowNotFoundException() {
        Long ownerId = 1L;
        Long itemId = 1L;

        when(userStorageStub.existsById(ownerId)).thenReturn(true);

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequest(ownerId, itemId));
    }

    @Test
    void whenMappingRequestWithItemsThenReturnDto() {
        ItemRequest itemRequest = new ItemRequest();
        List<Item> items = List.of(new Item());
        itemRequest.setItems(items);

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(1, itemRequestDto.getItems().size());
    }

}