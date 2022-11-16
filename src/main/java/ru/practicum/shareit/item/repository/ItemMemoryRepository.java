package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemMemoryRepository implements ItemRepository {
    private final Map<Long, Item> items;
    private Long counter;

    public ItemMemoryRepository() {
        items = new HashMap<>();
        counter = 1L;
    }

    @Override
    public Item save(Item item) {
        item.setId(item.getId() == null ? counter++ : item.getId());
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public List<Item> findByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findBySearch(String searchBy) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchBy.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchBy.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}
