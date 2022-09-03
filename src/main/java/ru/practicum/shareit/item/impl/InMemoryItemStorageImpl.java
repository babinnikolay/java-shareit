package ru.practicum.shareit.item.impl;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryItemStorageImpl {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 1L;

    public Item saveItem(Item item) {
        if (item.getId() == null) {
            item.setId(lastId++);
        }
        items.put(item.getId(), item);
        return item;
    }

    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    public Collection<Item> getAllItemsByUser(User user) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public Collection<Item> searchItems(String text) {
        String textInLowerCase = text.toLowerCase();
        return items.values()
                .stream()
                .filter(Item::isAvailable)
                .filter(text.isEmpty() ? item -> false :
                        item -> (item.getName().toLowerCase().contains(textInLowerCase)
                                || item.getDescription().toLowerCase().contains(textInLowerCase)))
                .collect(Collectors.toList());
    }
}
