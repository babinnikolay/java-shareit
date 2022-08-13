package ru.practicum.shareit.item.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) throws UserNotFoundException {
        User user = getUserById(userId);
        validateItem(itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemStorage.saveItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) throws UserNotFoundException, ItemNotFoundException {
        User user = getUserById(userId);
        Item item = itemStorage.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(String.format("Item with id %d not found", itemId));
        }
        if (item.getOwner() != user) {
            throw new UserNotFoundException(String.format("User with id %d not owner", itemId));
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        return ItemMapper.toItemDto(itemStorage.saveItem(item));
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) throws ItemNotFoundException {
        Item item = itemStorage.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(String.format("Item with id %d not found", itemId));
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getAllItemsByUser(Long userId) throws UserNotFoundException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with id %d not found", userId));
        }
        return itemStorage.getAllItemsByUser(user).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private User getUserById(Long userId) throws UserNotFoundException {
        User user = userStorage.getUserById(userId);
        if (userId == null || user == null) {
            throw new UserNotFoundException(String.format("user with id %s not found", userId));
        }
        return user;
    }

    private void validateItem(ItemDto itemDto) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
