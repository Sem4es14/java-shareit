package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingDbRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerOrderByStartDesc(User booker);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and " +
            "b.item = :item and " +
            "b.status = :status and " +
            "b.end < current_timestamp ")
    List<Booking> findByBookerAndItemAndStatus(User booker, Item item, BookingStatus status);

    @Query(value = "select b from Booking b " +
            "where b.status = :status and " +
            "      b.booker = :booker " +
            "order by b.start desc ")
    List<Booking> findByBookerStatus(User booker, BookingStatus status);

    @Query(value = "select b from Booking b " +
            "where b.end < current_timestamp and " +
            "      b.booker = :booker " +
            "order by b.start desc ")
    List<Booking> findByBookerPast(User booker);

    @Query(value = "select b from Booking b " +
            "where current_timestamp between b.start and b.end and " +
            "      b.booker = :booker " +
            "order by b.start desc ")
    List<Booking> findByBookerCurrent(User booker);

    @Query(value = "select b from Booking b " +
            "where b.start > current_timestamp and " +
            "      b.booker = :booker " +
            "order by b.start desc ")
    List<Booking> findByBookerFuture(User booker);

    List<Booking> findByItemOwnerOrderByStartDesc(User booker);

    @Query(value = "select b from Booking b " +
            "           join Item i on i = b.item " +
            "where b.status = :status and " +
            "      i.owner = :owner " +
            "order by b.start desc ")
    List<Booking> findByItemOwnerStatus(User owner, BookingStatus status);

    @Query(value = "select b from Booking b " +
            "           join Item i on i = b.item " +
            "where b.end < current_timestamp and " +
            "      i.owner = :owner " +
            "order by b.start desc ")
    List<Booking> findByItemOwnerPast(User owner);

    @Query(value = "select b from Booking b " +
            "           join Item i on i = b.item " +
            "where current_timestamp between b.start and b.end and " +
            "      i.owner = :owner " +
            "order by b.start desc ")
    List<Booking> findByItemOwnerCurrent(User owner);

    @Query(value = "select b from Booking b " +
            "           join Item i on i = b.item " +
            "where b.start > current_timestamp and " +
            "      i.owner = :owner " +
            "order by b.start desc ")
    List<Booking> findByItemOwnerFuture(User owner);

    Optional<Booking> findByItemAndStartAfter(Item item, LocalDateTime dateTime);

    Optional<Booking> findByItemAndEndBefore(Item item, LocalDateTime dateTime);

}
