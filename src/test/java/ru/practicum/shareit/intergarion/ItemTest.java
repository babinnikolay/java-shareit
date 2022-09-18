package ru.practicum.shareit.intergarion;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingService bookingService;
    private final BookingStorage bookingStorage;

    @AfterEach
    public void cleanTable() {
        em.createQuery("delete from Comment ").executeUpdate();
        em.createQuery("delete from Booking ").executeUpdate();
        em.createQuery("delete from Item ").executeUpdate();
        em.createQuery("delete from ItemRequest ").executeUpdate();
        em.createQuery("delete from User ").executeUpdate();
    }

    @Test
    void whenCreateItemThenSaveItToDb() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");

        ItemDto itemDto = createItemDto("name", "desc", true, userDto.getId());

        ItemDto newItemDto = itemService.createItem(itemDto, userDto.getId());

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id",
                Item.class);
        Item item = query.setParameter("id", newItemDto.getId()).getSingleResult();

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.isAvailable());
    }

    @Test
    void whenCreateItemWithRequestThenSaveItToDb() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto userRequest = createUserDto("name1", "email1@email.com");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("desc");
        ItemRequestDto newItemRequestDto = itemRequestService.createRequest(itemRequestDto, userRequest.getId());

        ItemDto itemDto = createItemDto("name", "desc", true, userDto.getId());
        itemDto.setRequestId(newItemRequestDto.getId());

        ItemDto newItemDto = itemService.createItem(itemDto, userDto.getId());

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id",
                Item.class);
        Item item = query.setParameter("id", newItemDto.getId()).getSingleResult();

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.isAvailable());
        assertEquals(itemDto.getRequestId(), item.getRequest().getId());
    }

    @Test
    void whenUpdateItemThenSaveItToDb() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        ItemDto itemDto = createItemDto("name", "desc", true, userDto.getId());

        ItemDto newItemDto = itemService.createItem(itemDto, userDto.getId());

        itemDto.setName("newName");
        itemDto.setDescription("newDesc");
        itemDto.setAvailable(false);

        ItemDto updatedItem = itemService.updateItem(itemDto, itemDto.getId(), userDto.getId());

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id",
                Item.class);
        Item item = query.setParameter("id", newItemDto.getId()).getSingleResult();

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.isAvailable());
    }

    @Test
    void whenGetItemThenGetItFromDb() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto booker = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name", "desc", true, userDto.getId());

        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = createBookingDto(now.plusSeconds(2), now.plusSeconds(4), itemDto.getId(), booker.getId());

        ItemDto itemDtoDb = itemService.getItem(itemDto.getId(), userDto.getId());

        assertEquals(itemDto.getId(), itemDtoDb.getId());
        assertEquals(booker.getId(), itemDtoDb.getLastBooking().getBookerId());
        assertEquals(bookingDto.getId(), itemDtoDb.getLastBooking().getId());
        assertEquals(booker.getId(), itemDtoDb.getNextBooking().getBookerId());
        assertEquals(bookingDto.getId(), itemDtoDb.getNextBooking().getId());
    }

    @Test
    void whenGetAllItemsByUserThenReturnListOfItemDto() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        createItemDto("name1", "desc1", true, userDto.getId());
        createItemDto("name2", "desc2", true, userDto.getId());

        List<ItemDto> itemsByUser = new ArrayList<>(itemService.getAllItemsByUser(userDto.getId(), 0, 2));

        assertEquals(2, itemsByUser.size());
        assertEquals("name1", itemsByUser.get(0).getName());
        assertEquals("name2", itemsByUser.get(1).getName());
    }

    @Test
    void whenSearchItemsThenGetListOfItemDto() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        createItemDto(")_)_)name1<><>", "*(*desc1*(&&*", true, userDto.getId());
        createItemDto("*(*(*name2 ()()", "&*&desc2_+(", true, userDto.getId());

        ArrayList<ItemDto> items = new ArrayList<>(itemService.searchItems("name1", 0, 2));
        assertEquals(1, items.size());
        assertEquals(")_)_)name1<><>", items.get(0).getName());

        items = new ArrayList<>(itemService.searchItems("desc2", 0, 2));
        assertEquals(1, items.size());
        assertEquals("*(*(*name2 ()()", items.get(0).getName());
    }

    @Test
    void whenCreateCommentThenSaveItToDb() throws NotFoundException, BadRequestException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto bookerDto = createUserDto("booker", "booker@email.com");
        ItemDto itemDto = createItemDto("name1", "desc1", true, userDto.getId());
        LocalDateTime now = LocalDateTime.now();

        User booker = new User();
        booker.setId(bookerDto.getId());
        booker.setName(bookerDto.getName());
        booker.setEmail(bookerDto.getEmail());

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(now.minusDays(2));
        booking.setEnd(now.minusDays(1));
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemDto.getId());
        booking.setItem(item);
        booking.setBooker(booker);

        createCommentDto("text1", itemDto.getId(), userDto.getId());
        createCommentDto("text2", itemDto.getId(), userDto.getId());

        TypedQuery<Comment> query = em.createQuery("SELECT i FROM Comment i ",
                Comment.class);
        List<Comment> resultList = query.getResultList();

        assertEquals(2, resultList.size());
        assertEquals("text1", resultList.get(0).getText());
        assertEquals("text2", resultList.get(1).getText());
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

    private CommentDto createCommentDto(String text, Long itemId, Long userId)
            throws NotFoundException, BadRequestException {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(text);
        return itemService.createComment(itemId, commentDto, userId);
    }

}
