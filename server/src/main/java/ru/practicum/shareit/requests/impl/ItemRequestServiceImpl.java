package ru.practicum.shareit.requests.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private static final String USER_MESSAGE = "User with id %s not found";

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto, Long userId) throws NotFoundException {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_MESSAGE, userId)));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestDto, LocalDateTime.now(), user);
        itemRequestStorage.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllRequestsByUserId(Long userId) throws NotFoundException {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format(USER_MESSAGE, userId));
        }
        return itemRequestStorage.findAllByUserIdOrderByCreated(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Long ownerId, Integer from, Integer size) throws NotFoundException {
        User owner = userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_MESSAGE, ownerId)));
        PageRequest page = PageRequest.of(from / size, size);
        return itemRequestStorage.findAllByOwnerIdOrderByCreated(owner, page)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getItemRequest(Long ownerId, Long itemId) throws NotFoundException {
        if (!userStorage.existsById(ownerId)) {
            throw new NotFoundException(String.format(USER_MESSAGE, ownerId));
        }
        ItemRequest itemRequest = itemRequestStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %s not found", itemId)));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }
}
