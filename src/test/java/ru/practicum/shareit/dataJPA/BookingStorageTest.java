package ru.practicum.shareit.dataJPA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingStorageTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingStorage bookingStorage;
    private User owner;
    private Item item;
    private Booking booking1;
    private Booking booking2;
    private User booker;

    @BeforeEach
    private void setUp() {
        owner = new User();
        owner.setName("name");
        owner.setEmail("email@email.com");

        booker = new User();
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        item = new Item();
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);

        booking1 = new Booking();
        booking1.setBooker(booker);

        booking2 = new Booking();
        booking2.setBooker(booker);
    }

    @Test
    void whenFindAllByOwnerAndStatusThenReturnListOfBooking() {
        owner = testEntityManager.merge(owner);

        item.setOwner(owner);
        item = testEntityManager.merge(item);
        booker = testEntityManager.persist(booker);

        booking1.setStatus(BookingStatus.APPROVED);
        booking1.setItem(item);
        booking1 = testEntityManager.merge(booking1);

        Set<BookingStatus> statuses = Set.of(BookingStatus.APPROVED, BookingStatus.WAITING);
        PageRequest page = PageRequest.of(0 / 2, 2);
        List<Booking> result
                = new ArrayList<>(bookingStorage.findAllByBookerIdAndStatusIn(booker.getId(), statuses, page));

        assertEquals(1, result.size());
        assertEquals(booking1.getStatus(), result.get(0).getStatus());
    }

    @Test
    void whenFindAllByOwnerAndStatusAndPastThenReturnListOfBooking() {
        owner = testEntityManager.merge(owner);

        item.setOwner(owner);
        item = testEntityManager.merge(item);
        booker = testEntityManager.persist(booker);

        LocalDateTime now = LocalDateTime.now();

        booking1.setStatus(BookingStatus.APPROVED);
        booking1.setItem(item);
        booking1.setStart(now.minusDays(2));
        booking1.setEnd(now.minusDays(1));
        booking1 = testEntityManager.persist(booking1);

        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(item);
        booking2.setStart(now.minusDays(4));
        booking2.setEnd(now.minusDays(3));
        booking2 = testEntityManager.persist(booking2);

        Set<BookingStatus> statuses = Set.of(BookingStatus.APPROVED, BookingStatus.WAITING);
        PageRequest page = PageRequest.of(0 / 2, 2);
        List<Booking> result
                = new ArrayList<>(bookingStorage.findAllByOwnerAndStatusInAndPastOrderByStartDesc(owner.getId(),
                statuses, LocalDateTime.now(), page));

        assertEquals(2, result.size());
        assertEquals(booking1.getStart(), result.get(0).getStart());
        assertEquals(booking2.getStart(), result.get(1).getStart());
    }

    @Test
    void whenFindLastBookingByItemIdThenReturnListOfBooking() {
        owner = testEntityManager.merge(owner);

        item.setOwner(owner);
        item = testEntityManager.merge(item);
        booker = testEntityManager.persist(booker);

        LocalDateTime now = LocalDateTime.now();

        booking1.setStatus(BookingStatus.APPROVED);
        booking1.setItem(item);
        booking1.setStart(now.minusDays(2));
        booking1.setEnd(now.minusDays(1));
        booking1 = testEntityManager.persist(booking1);

        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(item);
        booking2.setStart(now.minusDays(4));
        booking2.setEnd(now.minusDays(3));
        booking2 = testEntityManager.persist(booking2);

        Set<BookingStatus> statuses = Set.of(BookingStatus.APPROVED, BookingStatus.WAITING);
        List<Booking> result
                = new ArrayList<>(bookingStorage.findLastBookingByItemId(item.getId(), owner.getId()));


        assertEquals(2, result.size());
        assertEquals(booking2.getEnd(), result.get(0).getEnd());
    }

    @Test
    void whenFindNextBookingByItemIdThenReturnListOfBooking() {
        owner = testEntityManager.merge(owner);

        item.setOwner(owner);
        item = testEntityManager.merge(item);
        booker = testEntityManager.persist(booker);

        LocalDateTime now = LocalDateTime.now();

        booking1.setStatus(BookingStatus.APPROVED);
        booking1.setItem(item);
        booking1.setStart(now.plusDays(1));
        booking1.setEnd(now.plusDays(2));
        booking1 = testEntityManager.persist(booking1);

        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setItem(item);
        booking2.setStart(now.plusDays(3));
        booking2.setEnd(now.plusDays(4));
        booking2 = testEntityManager.persist(booking2);

        Set<BookingStatus> statuses = Set.of(BookingStatus.APPROVED, BookingStatus.WAITING);
        List<Booking> result
                = new ArrayList<>(bookingStorage.findNextBookingByItemId(item.getId(), owner.getId(),
                LocalDateTime.now()));


        assertEquals(2, result.size());
        assertEquals(booking2.getStart(), result.get(0).getStart());
    }
}
