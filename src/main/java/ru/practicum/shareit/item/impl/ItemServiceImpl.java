package ru.practicum.shareit.item.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) throws NotFoundException {
        User user = getUserById(userId);
        validateItem(itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestStorage.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException(String.format("Request by id %s not found", itemDto.getRequestId())));
            item.setRequest(itemRequest);
        }
        itemStorage.save(item);
        itemDto.setId(item.getId());
        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) throws NotFoundException {
        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d not found", itemId))
        );

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("User with id %d is not owner", itemId));
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
        itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) throws NotFoundException {
        Item item = itemStorage.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Item with id %d not found", itemId)));
        item.setLastBooking(getLastItemBooking(itemId, userId));
        item.setNextBooking(getNextItemBooking(itemId, userId));
        item.setComments(getItemComments(itemId));

        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getAllItemsByUser(Long userId, Integer from, Integer size) throws NotFoundException {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        PageRequest page = PageRequest.of(from / size, size);
        List<ItemDto> list = new ArrayList<>();
        for (Item item : itemStorage.findAllByOwnerOrderById(user, page)) {
            item.setNextBooking(getNextItemBooking(item.getId(), userId));
            item.setLastBooking(getLastItemBooking(item.getId(), userId));
            ItemDto itemDto = ItemMapper.toItemDto(item);
            list.add(itemDto);
        }
        return list;
    }

    @Override
    public Collection<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        PageRequest page = PageRequest.of(from / size, size);
        return itemStorage
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, page)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long itemId, CommentDto commentDto, Long userId)
            throws NotFoundException, BadRequestException {
        validateComment(commentDto);
        User author = getUserById(userId);
        Item item = itemStorage.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d not found", itemId)
                ));
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = bookingStorage.findBookingByItemIdAndStatusAndStartBefore(
                itemId, BookingStatus.APPROVED, now);
        if (bookings.isEmpty()) {
            throw new BadRequestException(String.format("Item with id %d does not have correct booking", itemId));
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);
        return CommentMapper.toDto(commentStorage.save(comment));
    }

    private Set<Comment> getItemComments(Long itemId) {
        return commentStorage.findAllByItemId(itemId);
    }

    private User getUserById(Long userId) throws NotFoundException {
        return userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("user with id %s not found", userId)));
    }

    private void validateItem(ItemDto itemDto) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Booking getLastItemBooking(Long itemId, Long userId) {
        List<Booking> lastBooking = bookingStorage.findLastBookingByItemId(itemId, userId);
        if (!lastBooking.isEmpty()) {
            return lastBooking.get(0);
        }
        return null;
    }

    private Booking getNextItemBooking(Long itemId, Long userId) {
        List<Booking> nextBooking = bookingStorage.findNextBookingByItemId(itemId, userId, LocalDateTime.now());
        if (!nextBooking.isEmpty()) {
            return nextBooking.get(0);
        }
        return null;
    }

    private void validateComment(CommentDto commentDto) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
