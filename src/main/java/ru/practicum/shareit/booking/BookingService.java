package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long userId) throws NotFoundException, BadRequestException;

    BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) throws NotFoundException, BadRequestException;

    BookingDto getBookingById(Long bookingId, Long userId) throws NotFoundException;

    Collection<BookingDto> getAllBookingsByBookerId(Long bookerId, BookingState state) throws NotFoundException;

    Collection<BookingDto> getAllBookingsByOwnerId(Long ownerId, BookingState state) throws NotFoundException;
}
