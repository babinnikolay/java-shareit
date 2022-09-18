package ru.practicum.shareit.intergarion;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private BookingDto bookingDto;
    private Long userId;
    private Long itemId;

    @AfterEach
    public void cleanTable() {
        em.createQuery("delete from Comment ").executeUpdate();
        em.createQuery("delete from Booking ").executeUpdate();
        em.createQuery("delete from Item ").executeUpdate();
        em.createQuery("delete from ItemRequest ").executeUpdate();
        em.createQuery("delete from User ").executeUpdate();
    }

    @Test
    void whenCreateBookingThenSaveItToDb() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name1", "desc1", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        BookingDto bookingDto = createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto.getId(), bookerDto.getId());

        TypedQuery<Booking> query = em.createQuery("SELECT i FROM Booking i WHERE i.id = :id",
                Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void whenApproveBookingThenSaveItToDb() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name1", "desc1", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        BookingDto bookingDto = createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto.getId(), bookerDto.getId());

        bookingService.approveBooking(bookingDto.getId(), userDto.getId(), true);

        TypedQuery<Booking> query = em.createQuery("SELECT i FROM Booking i WHERE i.id = :id",
                Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void whenRejectedBookingThenSaveItToDb() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name1", "desc1", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        BookingDto bookingDto = createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto.getId(), bookerDto.getId());

        bookingService.approveBooking(bookingDto.getId(), userDto.getId(), false);

        TypedQuery<Booking> query = em.createQuery("SELECT i FROM Booking i WHERE i.id = :id",
                Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertEquals(BookingStatus.REJECTED, booking.getStatus());
    }

    @Test
    void whenGetBookingByIdThenGetIt() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name1", "desc1", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        BookingDto bookingDto = createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto.getId(), bookerDto.getId());

        BookingDto bookingDtoDb = bookingService.getBookingById(bookingDto.getId(), userDto.getId());

        assertEquals(bookingDto.getId(), bookingDtoDb.getId());
    }

    @Test
    void whenGetAllBookingByBookerThenReturnListOfBooking() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto1 = createItemDto("name1", "desc1", true, userDto.getId());
        ItemDto itemDto2 = createItemDto("name2", "desc2", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto1.getId(), bookerDto.getId());
        createBookingDto(now.plusHours(3), now.plusHours(4),
                itemDto2.getId(), bookerDto.getId());

        List<BookingDto> listAll = new ArrayList<>(bookingService.getAllBookingsByBookerId(bookerDto.getId(),
                BookingState.ALL, 0, 2));
        List<BookingDto> listPast = new ArrayList<>(bookingService.getAllBookingsByBookerId(bookerDto.getId(),
                BookingState.PAST, 0, 2));

        assertEquals(2, listAll.size());
        assertEquals(0, listPast.size());
    }

    @Test
    void whenGetAllBookingByOwnerThenReturnListOfBooking() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto1 = createItemDto("name1", "desc1", true, userDto.getId());
        ItemDto itemDto2 = createItemDto("name2", "desc2", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        createBookingDto(now.plusHours(1), now.plusHours(2),
                itemDto1.getId(), bookerDto.getId());
        createBookingDto(now.plusHours(3), now.plusHours(4),
                itemDto2.getId(), bookerDto.getId());

        List<BookingDto> listAll = new ArrayList<>(bookingService.getAllBookingsByOwnerId(userDto.getId(),
                BookingState.ALL, 0, 2));
        List<BookingDto> listPast = new ArrayList<>(bookingService.getAllBookingsByOwnerId(userDto.getId(),
                BookingState.PAST, 0, 2));

        assertEquals(2, listAll.size());
        assertEquals(0, listPast.size());
    }

    private BookingDto createBookingDto(LocalDateTime start, LocalDateTime end, Long itemId, Long bookerId)
            throws NotFoundException, BadRequestException {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItemId(itemId);
        return bookingService.createBooking(bookingDto, bookerId);
    }

    private UserDto createUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);

        return userService.createUser(userDto);
    }

    private ItemDto createItemDto(String name, String description, boolean available, Long userId) throws NotFoundException {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        return itemService.createItem(itemDto, userId);
    }

}
