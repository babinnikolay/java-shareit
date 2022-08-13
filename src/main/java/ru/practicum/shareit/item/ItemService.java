package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId) throws UserNotFoundException;

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) throws UserNotFoundException, ItemNotFoundException;

    ItemDto getItem(Long itemId, Long userId) throws ItemNotFoundException;

    Collection<ItemDto> getAllItemsByUser(Long userId) throws UserNotFoundException;

    Collection<ItemDto> searchItems(String text) throws UserNotFoundException;
}
