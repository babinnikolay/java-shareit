package ru.practicum.shareit.intergarion;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserTest {
    private final EntityManager em;
    private final UserService userService;

    @AfterEach
    public void cleanTable() {
        em.createQuery("delete from User ").executeUpdate();
    }

    @Test
    void whenCreateUserThenSaveItToDb() {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("email@email.com");

        UserDto newUserDto = userService.createUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id",
                User.class);
        User user = query.setParameter("id", newUserDto.getId()).getSingleResult();

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void whenUpdateUserThenSaveItToDb() throws ConflictException, NotFoundException {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("email@email.com");

        UserDto newUserDto = userService.createUser(userDto);

        userDto.setName("newUser");
        userDto.setEmail("newEmail@email.com");

        userService.updateUser(newUserDto.getId(), userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id",
                User.class);
        User user = query.setParameter("id", newUserDto.getId()).getSingleResult();

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void whenGetUserByIdThenGetIt() throws NotFoundException {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("email@email.com");

        UserDto newUserDto = userService.createUser(userDto);

        UserDto userDtoDb = userService.getUserById(newUserDto.getId());

        assertEquals(newUserDto.getName(), userDtoDb.getName());
        assertEquals(newUserDto.getEmail(), userDtoDb.getEmail());
        assertEquals(newUserDto.getId(), userDtoDb.getId());
    }

    @Test
    void whenDeleteUserByIdThenDeleteIt() throws NotFoundException {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("email@email.com");

        UserDto newUserDto = userService.createUser(userDto);

        userService.deleteUserById(newUserDto.getId());

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id",
                User.class);
        TypedQuery<User> typedQuery = query.setParameter("id", newUserDto.getId());

        assertThrows(NoResultException.class, typedQuery::getSingleResult);
    }

    @Test
    void whenGetAllUsersThenReturnListOfUsers() {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("email@email.com");
        UserDto newUserDto = userService.createUser(userDto);

        UserDto userDto2 = new UserDto();
        userDto2.setName("user2");
        userDto2.setEmail("email2@email.com");
        UserDto newUserDto2 = userService.createUser(userDto2);

        List<UserDto> allUsers = List.copyOf(userService.getAllUsers());

        assertEquals(2, allUsers.size());

        assertEquals(userDto.getName(), allUsers.get(0).getName());
        assertEquals(userDto.getEmail(), allUsers.get(0).getEmail());

        assertEquals(userDto2.getName(), allUsers.get(1).getName());
        assertEquals(userDto2.getEmail(), allUsers.get(1).getEmail());
    }

}
