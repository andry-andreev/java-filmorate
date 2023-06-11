package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;
    private Map<User, Set<User>> friends = new HashMap<>();
    private List<User> users = new ArrayList<>();

    public void addFriend(int userId, int friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        friends.computeIfAbsent(user, k -> new HashSet<>()).add(friend);
        friends.computeIfAbsent(friend, k -> new HashSet<>()).add(user);
    }

    public void removeFriend(int userId, int friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        friends.computeIfAbsent(user, k -> new HashSet<>()).remove(friend);
        friends.computeIfAbsent(friend, k -> new HashSet<>()).remove(user);
    }

    public Set<User> getFriends(int userId) {
        Comparator<User> comparator = Comparator.comparingInt(User::getId);
        Set<User> sortedFriends = new TreeSet<>(comparator);
        sortedFriends.addAll(friends.get(inMemoryUserStorage.getUserById(userId)));
        return sortedFriends;
    }

    public Set<User> getMutualFriends(User user1, User user2) {
        Set<User> mutualFriends = new HashSet<>(friends.getOrDefault(user1, Collections.emptySet()));
        mutualFriends.retainAll(friends.getOrDefault(user2, Collections.emptySet()));
        return mutualFriends;
    }
}
