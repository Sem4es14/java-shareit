package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserMemoryRepository implements UserRepository {
    private final Map<Long, User> users;
    private Long counter;

    public UserMemoryRepository() {
        users = new HashMap<>();
        counter = 1L;
    }

    @Override
    public User save(User user) {
        user.setId(user.getId() == null ? counter++ : user.getId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }
}
