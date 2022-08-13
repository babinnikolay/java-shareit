package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public Item saveItem(Item item) {
        if (item.getId() == null) {
            item.setId(lastId++);
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getAllItemsByUser(User user) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner() == user)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return items.values()
                .stream()
                .filter(Item::isAvailable)
                .filter(text.isEmpty() ? item -> false : item ->
                        (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

}
