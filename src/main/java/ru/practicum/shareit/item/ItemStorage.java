package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemStorage {
    Item saveItem(Item item);

    Item getItemById(Long itemId);

    Collection<Item> getAllItemsByUser(User user);

    Collection<Item> searchItems(String text);
}
