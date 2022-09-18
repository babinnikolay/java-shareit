package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetItemRequestTest {

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
}
