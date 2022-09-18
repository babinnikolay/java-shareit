package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookingByIdTest {

    private BookingService bookingService;
    @Mock
    private BookingStorage bookingStorageStub;
    @Mock
    private UserStorage userStorageStub;
    @Mock
    private ItemStorage itemStorageStub;
    private User user;
    private Item item;
    private Long bookingId;
    private Booking booking;
    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingStorageStub, userStorageStub, itemStorageStub);

        BookingDto bookingDto = new BookingDto();
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        bookingDto.setStart(now);
        bookingDto.setEnd(now.plusHours(2));
        bookingDto.setItemId(1L);
        user = new User();
        item = new Item();
        bookingId = 1L;
        Long ownerId = 1L;
        booking = new Booking();
        owner = new User();
        booker = new User();
    }

    @Test
    void whenGetBookingNonExistsBookingThenThrowNotFoundException() {
        Long userId = 1L;

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(2L, userId));
    }

    @Test
    void whenGetBookingNonExistsUserThenThrowNotFoundException() {
        Long userId = 1L;

        when(bookingStorageStub.findById(2L)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(2L, userId));
    }

    @Test
    void whenGetBookingNonOwnerThenThrowNotFoundException() {
        Long userId = 1L;

        item.setId(1000L);
        owner.setId(100L);
        item.setOwner(owner);
        booker.setId(200L);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingStorageStub.findById(1L)).thenReturn(Optional.of(booking));
        when(userStorageStub.findById(1L)).thenReturn(Optional.of(user));

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(bookingId, userId));
        assertEquals("User with id 1 does not have rights to item id 1000", e.getMessage());
    }
}