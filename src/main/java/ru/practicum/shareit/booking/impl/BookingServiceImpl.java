package ru.practicum.shareit.booking.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long userId)
            throws NotFoundException, BadRequestException {
        validateBooking(bookingDto);
        User user = getUserById(userId);
        Item item = getItemById(bookingDto.getItemId());
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException(String.format("User with id %s is owner item id %s", userId, item.getId()));
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking save = bookingStorage.save(booking);
        return BookingMapper.toBookingDto(save);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved)
            throws NotFoundException, BadRequestException {
        User owner = getUserById(ownerId);

        Optional<Booking> optional = bookingStorage.findById(bookingId);
        if (bookingId == null || optional.isEmpty()) {
            throw new NotFoundException(String.format("Booking with id %s not found", bookingId));
        }
        Booking booking = optional.get();
        if (booking.getStatus() == BookingStatus.APPROVED && Boolean.TRUE.equals(approved)) {
            throw new BadRequestException(String.format("Booking with id %s already approved", bookingId));
        }
        Item item = booking.getItem();

        if (!item.getOwner().getId().equals(owner.getId())) {
            throw new NotFoundException(String.format("User with id %s does not owner item id %s",
                    ownerId, booking.getItem().getId()));
        }
        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingStorage.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId)
            throws NotFoundException {
        Optional<Booking> optional = bookingStorage.findById(bookingId);
        getUserById(userId);

        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Booking with id %s not found", bookingId));
        }
        Booking booking = optional.get();
        if (!booking.getItem().getOwner().getId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException(String.format("User with id %s does not have rights to item id %s",
                    userId, booking.getItem().getId()));
        }
        return BookingMapper.toBookingDto(optional.get());
    }

    @Override
    public Collection<BookingDto> getAllBookingsByBookerId(Long bookerId, BookingState state)
            throws NotFoundException {
        getUserById(bookerId);
        Collection<Booking> bookings;

        Set<BookingStatus> statuses = getStatusesByBookingState(state);
        if (state == BookingState.PAST) {
            bookings = bookingStorage.findAllByBookerIdAndStatusInAndStartBeforeOrderByStartDesc(
                    bookerId, statuses, LocalDateTime.now());
        } else {
            bookings = bookingStorage.findAllByBookerIdAndStatusInOrderByStartDesc(bookerId, statuses);
        }

        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getAllBookingsByOwnerId(Long ownerId, BookingState state) throws NotFoundException {
        getUserById(ownerId);
        Collection<Booking> bookings;

        Set<BookingStatus> statuses = getStatusesByBookingState(state);
        if (state == BookingState.PAST) {
            bookings = bookingStorage.findAllByOwnerAndStatusInAndPastOrderByStartDesc(
                    ownerId, statuses, LocalDateTime.now());
        } else {
            bookings = bookingStorage.findAllByOwnerAndStatusInOrderByStartDesc(ownerId, statuses);
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) throws NotFoundException {
        Optional<User> optional = userStorage.findById(userId);
        if (userId == null || optional.isEmpty()) {
            throw new NotFoundException(String.format("user with id %s not found", userId));
        }
        return optional.get();
    }

    private Item getItemById(Long itemId) throws NotFoundException, BadRequestException {
        Optional<Item> optional = itemStorage.findById(itemId);
        if (itemId == null || optional.isEmpty()) {
            throw new NotFoundException(String.format("Item with id %s not found", itemId));
        }
        Item item = optional.get();
        if (!item.isAvailable()) {
            throw new BadRequestException(String.format("Item with id %s not available", itemId));
        }
        return optional.get();
    }

    private void validateBooking(BookingDto bookingDto) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Set<BookingStatus> getStatusesByBookingState(BookingState state) {
        Set<BookingStatus> statuses = new HashSet<>();
        if (state == BookingState.ALL) {
            statuses.addAll(Set.of(BookingStatus.values()));
        }
        if (state == BookingState.CURRENT) {
            statuses.addAll(Set.of(BookingStatus.WAITING, BookingStatus.REJECTED));
        }
        if (state == BookingState.PAST) {
            statuses.add(BookingStatus.APPROVED);
        }
        if (state == BookingState.FUTURE) {
            statuses.addAll(Set.of(BookingStatus.WAITING, BookingStatus.APPROVED));
        }
        if (state == BookingState.WAITING) {
            statuses.add(BookingStatus.WAITING);
        }
        if (state == BookingState.REJECTED) {
            statuses.add(BookingStatus.REJECTED);
        }
        return statuses;
    }
}
