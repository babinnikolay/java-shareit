package ru.practicum.shareit.user.impl;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImMemoryUserStorageImpl {

    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 1L;

    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(lastId++);
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUserByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(Long userId) {
        return users.get(userId);
    }

    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }
}
