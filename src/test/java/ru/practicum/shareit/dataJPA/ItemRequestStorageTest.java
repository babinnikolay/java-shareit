package ru.practicum.shareit.dataJPA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestStorageTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRequestStorage itemRequestStorage;
    private User owner;
    private Item item;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    private void setUp() {
        owner = new User();
        owner.setName("name");
        owner.setEmail("email@email.com");

        user = new User();
        user.setName("user");
        user.setEmail("user@email.com");

        item = new Item();
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);

        itemRequest = new ItemRequest();

        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setDescription("desc");
    }

    @Test
    void whenFindAllByOwnerAndStatusThenReturnListOfBooking() {
        owner = testEntityManager.merge(owner);
        user = testEntityManager.merge(user);

        itemRequest.setUser(user);
        testEntityManager.persist(itemRequest);

        item.setOwner(owner);
        item.setRequest(itemRequest);
        item = testEntityManager.merge(item);

        Set<BookingStatus> statuses = Set.of(BookingStatus.APPROVED, BookingStatus.WAITING);
        PageRequest page = PageRequest.of(0 / 2, 2);
        List<ItemRequest> result
                = new ArrayList<>(itemRequestStorage.findAllByOwnerIdOrderByCreated(owner, page));

        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).getUser().getName());
    }
}
