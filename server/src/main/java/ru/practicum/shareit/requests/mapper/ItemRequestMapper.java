package ru.practicum.shareit.requests.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setDescription(itemRequest.getDescription());
        if (itemRequest.getItems() == null) {
            itemRequestDto.setItems(Collections.emptyList());
        } else {
            itemRequestDto.setItems(itemRequest.getItems()
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList()));
        }
        return itemRequestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, LocalDateTime time, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setCreated(time);
        itemRequest.setUser(user);
        return itemRequest;
    }
}
