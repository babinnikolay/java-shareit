package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerOrderById(User owner, PageRequest page);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(
            String name, String description, PageRequest page);

}
