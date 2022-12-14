package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.constant.Constant;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException, BadRequestException {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestHeader(Constant.USER_ID_HEADER) Long userId,
                                     @RequestParam boolean approved)
            throws NotFoundException, BadRequestException {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsByBookerId(
            @RequestHeader(Constant.USER_ID_HEADER) Long bookerId,
            @RequestParam BookingState state,
            @RequestParam Integer from,
            @RequestParam Integer size)
            throws NotFoundException {
        return bookingService.getAllBookingsByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsByOwnerId(
            @RequestHeader(Constant.USER_ID_HEADER) Long ownerId,
            @RequestParam BookingState state,
            @RequestParam Integer from,
            @RequestParam Integer size)
            throws NotFoundException {
        return bookingService.getAllBookingsByOwnerId(ownerId, state, from, size);
    }
}


