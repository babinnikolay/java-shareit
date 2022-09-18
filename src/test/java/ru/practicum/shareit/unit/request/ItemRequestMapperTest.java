package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {
    private ItemRequestMapper itemRequestMapper;

    @Test
    void whenMappingRequestWithItemsThenReturnDto() {
        ItemRequest itemRequest = new ItemRequest();
        List<Item> items = List.of(new Item());
        itemRequest.setItems(items);

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(1, itemRequestDto.getItems().size());
    }

}