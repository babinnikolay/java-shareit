package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ImMemoryUserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(lastId++);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> first = users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        if (first.isEmpty()) {
            return null;
        }
        return first.get();
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
