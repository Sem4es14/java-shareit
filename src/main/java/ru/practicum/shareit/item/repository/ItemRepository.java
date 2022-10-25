package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findByOwner(Long ownerId);

    List<Item> findBySearch(String searchBy);

    void delete(Long id);

    List<Item> getAll();
}
