package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetAllBookingByOwnerTest {

    private BookingService bookingService;
    @Mock
    private BookingStorage bookingStorageStub;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemStorage itemStorageStub;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingStorageStub, userStorageStub, itemStorageStub);
    }

    @Test
    void whenGetAllBookingsNonExistsOwnerThenThrowNotFoundException() {
        Long userId = 1L;

        assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingsByOwnerId(userId, BookingState.ALL, 0, 1));
    }
}