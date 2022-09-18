package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookingTest {

    private BookingService bookingService;
    @Mock
    private BookingStorage bookingStorageStub;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemStorage itemStorageStub;
    private BookingDto bookingDto;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingStorageStub, userStorageStub, itemStorageStub);

        bookingDto = new BookingDto();
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        bookingDto.setStart(now);
        bookingDto.setEnd(now.plusHours(2));
        bookingDto.setItemId(1L);
        user = new User();
        item = new Item();
    }

    @Test
    void whenCreateBookingNonExistsUserThenThrowNotFoundException() {
        Long userId = 1L;

        when(userStorageStub.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    void whenCreateBookingNonExistsItemThenThrowNotFoundException() {
        Long userId = 1L;
        bookingDto.setId(1L);

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    void whenCreateBookingNonAvailableItemThenThrowBadRequestException() {
        Long userId = 1L;
        bookingDto.setId(1L);
        item.setAvailable(false);

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(itemStorageStub.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    void whenCreateBookingStartBeforeNowThenThrowBadRequestException() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.minusDays(1));

        assertThrows(ConstraintViolationException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    void whenCreateBookingEndBeforeStartThenThrowBadRequestException() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.plusHours(1));
        bookingDto.setEnd(now);

        assertThrows(ConstraintViolationException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }

    @Test
    void whenCreateBookingEndStartEqualsThenThrowBadRequestException() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.plusHours(1));
        bookingDto.setEnd(now.plusHours(1));

        assertThrows(ConstraintViolationException.class,
                () -> bookingService.createBooking(bookingDto, userId));
    }
}