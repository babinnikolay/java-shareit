package ru.practicum.shareit.requests;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestDto requestDto, Long userId) throws NotFoundException;

    Collection<ItemRequestDto> getAllRequestsByUserId(Long userId) throws NotFoundException;

    Collection<ItemRequestDto> getAllRequests(Long ownerId, Integer from, Integer size) throws NotFoundException;

    ItemRequestDto getItemRequest(Long ownerId, Long itemId) throws NotFoundException;
}
