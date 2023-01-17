package ru.osipov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.osipov.controller.PostController;
import ru.osipov.repository.PostRepository;
import ru.osipov.service.PostService;

@Configuration
public class Config {
    @Bean
    public PostController postController() {
        return new PostController(postService());
    }

    @Bean
    public PostService postService() {
        return new PostService(postRepository());
    }

    @Bean
    public PostRepository postRepository() {
        return new PostRepository();
    }
}