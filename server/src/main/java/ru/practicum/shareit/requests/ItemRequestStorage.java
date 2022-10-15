package ru.practicum.shareit.requests;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByUserIdOrderByCreated(Long userId);

    @Query(value = "select ir from ItemRequest as ir " +
            "inner join Item as it " +
            "on ir = it.request " +
            "where it.owner = ?1 " +
            "order by ir.created")
    Collection<ItemRequest> findAllByOwnerIdOrderByCreated(User owner, PageRequest pageable);
}
