package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User saveUser(User user);

    User getUserByEmail(String email);

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    Collection<User> getAllUsers();
}
