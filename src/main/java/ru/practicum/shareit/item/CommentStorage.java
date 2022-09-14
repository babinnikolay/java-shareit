package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByItemId(Long itemId);
}
