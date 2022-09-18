package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId) throws NotFoundException;

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) throws  NotFoundException;

    ItemDto getItem(Long itemId, Long userId) throws NotFoundException;

    Collection<ItemDto> getAllItemsByUser(Long userId, Integer from, Integer size) throws NotFoundException;

    Collection<ItemDto> searchItems(String text, Integer from, Integer size) throws NotFoundException;

    CommentDto createComment(Long itemId, CommentDto commentDto, Long userId) throws NotFoundException, BadRequestException;
}
