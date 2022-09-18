package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequestException;
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
class ApproveBookingTest {

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
    private Long bookingId;
    private Long ownerId;
    private Booking booking;
    private User owner;

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
        bookingId = 1L;
        ownerId = 1L;
        booking = new Booking();
        owner = new User();
    }

    @Test
    void whenApproveBookingNonExistsUserThenThrowNotFoundException() {
        Long userId = 1L;

        when(userStorageStub.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(bookingId, ownerId, true));
    }

    @Test
    void whenApproveBookingNonExistsBookingThenThrowNotFoundException() {
        Long userId = 1L;
        bookingDto.setId(1L);

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(bookingStorageStub.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(bookingId, ownerId, true));
    }

    @Test
    void whenApproveApprovedBookingThenThrowBadRequestException() {
        Long userId = 1L;
        booking.setStatus(BookingStatus.APPROVED);

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(bookingStorageStub.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(BadRequestException.class,
                () -> bookingService.approveBooking(bookingId, userId, true));
    }

    @Test
    void whenApproveBookingNotOwnerThenThrowNotFoundException() {
        Long userId = 1L;
        booking.setItem(item);
        item.setId(2L);
        item.setOwner(owner);
        owner.setId(100L);

        when(userStorageStub.findById(userId)).thenReturn(Optional.of(user));
        when(bookingStorageStub.findById(bookingId)).thenReturn(Optional.of(booking));

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(bookingId, 1L, true));
        assertEquals("User with id 1 does not owner item id 2", e.getMessage());
    }
}