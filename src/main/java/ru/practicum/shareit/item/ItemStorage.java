package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerOrderById(User owner);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(
            String name, String description);

}
