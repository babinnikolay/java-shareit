package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status in ?2 order by b.start desc")
    Collection<Booking> findAllByOwnerAndStatusInOrderByStartDesc(Long ownerId, Set<BookingStatus> statuses);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status in ?2 and b.start < ?3 order by b.start desc")
    Collection<Booking> findAllByOwnerAndStatusInAndPastOrderByStartDesc(
            Long ownerId, Set<BookingStatus> statuses, LocalDateTime past);

    Collection<Booking> findAllByBookerIdAndStatusInOrderByStartDesc(Long ownerId, Set<BookingStatus> statuses);

    Collection<Booking> findAllByBookerIdAndStatusInAndStartBeforeOrderByStartDesc(
            Long ownerId, Set<BookingStatus> statuses, LocalDateTime past);

    @Query("select b from Booking b where b.item.id = ?1 and b.item.owner.id = ?2 order by b.end ")
    List<Booking> findLastBookingByItemId(Long itemId, Long ownerId);

    @Query("select b from Booking b where b.item.id = ?1 and b.item.owner.id = ?2 and b.start > ?3 order by b.start desc ")
    List<Booking> findNextBookingByItemId(Long itemId, Long ownerId, LocalDateTime now);

    Collection<Booking> findBookingByItemIdAndStatusAndStartBefore(Long itemId, BookingStatus status, LocalDateTime date);
}
