package ru.practicum.shareit.intergarion;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;

    @AfterEach
    public void cleanTable() {
        em.createQuery("delete from Comment ").executeUpdate();
        em.createQuery("delete from Booking ").executeUpdate();
        em.createQuery("delete from Item ").executeUpdate();
        em.createQuery("delete from ItemRequest ").executeUpdate();
        em.createQuery("delete from User ").executeUpdate();
    }

    @Test
    void whenCreateItemRequestThenSaveItToDb() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        ItemRequestDto itemRequestDto = createRequestDto("desc", userDto.getId());

        ItemRequestDto newItemRequestDto = itemRequestService.createRequest(itemRequestDto, userDto.getId());

        TypedQuery<ItemRequest> query = em.createQuery("SELECT i FROM ItemRequest i WHERE i.id = :id",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", newItemRequestDto.getId()).getSingleResult();

        assertEquals(newItemRequestDto.getId(), itemRequest.getId());
        assertEquals(newItemRequestDto.getDescription(), itemRequest.getDescription());
    }

    @Test
    void whenGetAllRequestByUserIdThenReturnListOfRequests() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        ItemRequestDto itemRequestDto1 = createRequestDto("desc1", userDto.getId());
        ItemRequestDto itemRequestDto2 = createRequestDto("desc2", userDto.getId());

        ArrayList<ItemRequestDto> listRequests
                = new ArrayList<>(itemRequestService.getAllRequestsByUserId(userDto.getId()));

        assertEquals(2, listRequests.size());
    }

    @Test
    void whenGetAllRequestsThenReturnListOfRequests() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto ownerDto = createUserDto("owner", "owner@email.com");
        ItemRequestDto itemRequestDto1 = createRequestDto("desc1", userDto.getId());
        ItemDto itemDto = createItemDto("name", "desc", true,
                ownerDto.getId(), itemRequestDto1.getId());


        ArrayList<ItemRequestDto> listRequests
                = new ArrayList<>(itemRequestService.getAllRequests(ownerDto.getId(), 0, 2));

        assertEquals(1, listRequests.size());
        assertEquals("desc1", listRequests.get(0).getDescription());
    }

    @Test
    void whenGetItemRequestThenReturnIt() throws NotFoundException {
        UserDto userDto = createUserDto("name", "email@email.com");
        UserDto ownerDto = createUserDto("owner", "owner@email.com");
        ItemRequestDto itemRequestDto1 = createRequestDto("desc1", userDto.getId());
        ItemDto itemDto = createItemDto("name", "desc", true,
                ownerDto.getId(), itemRequestDto1.getId());

        ItemRequestDto itemRequest = itemRequestService.getItemRequest(ownerDto.getId(), itemRequestDto1.getId());

        assertEquals("desc1", itemRequest.getDescription());
    }

    private UserDto createUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);

        return userService.createUser(userDto);
    }

    private ItemDto createItemDto(String name, String description, boolean available, Long userId, Long requestId)
            throws NotFoundException {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setRequestId(requestId);

        return itemService.createItem(itemDto, userId);
    }

    private ItemRequestDto createRequestDto(String description, Long userId) throws NotFoundException {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(description);

        return itemRequestService.createRequest(itemRequestDto, userId);
    }
}
