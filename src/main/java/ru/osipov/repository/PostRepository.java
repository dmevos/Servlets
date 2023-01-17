package ru.osipov.repository;

import ru.osipov.exception.NotFoundException;
import ru.osipov.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private static final AtomicLong counter = new AtomicLong(0);
    private static final ConcurrentHashMap<Long, Post> repo = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(repo.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repo.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(counter.incrementAndGet());
            repo.put(counter.get(), post);
            return post;
        } else {
            if (repo.containsKey(post.getId())) {
                repo.put(post.getId(), post);
            } else {
                throw new NotFoundException("Жопа");
            }
        }
        return post;
    }

    public void removeById(long id) {
        repo.remove(id);
    }
}