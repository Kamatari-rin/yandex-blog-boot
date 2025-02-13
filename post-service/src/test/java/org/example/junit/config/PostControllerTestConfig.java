package org.example.junit.config;

import org.example.controller.CommentController;
import org.example.controller.PostController;
import org.example.service.CommentService;
import org.example.service.PostService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("org.example.controller")
public class PostControllerTestConfig {

    @Bean
    @Primary
    public PostService mockPostService() {
        return Mockito.mock(PostService.class);
    }

    @Bean
    @Primary
    public PostController postController(PostService postService) {
        return new PostController(postService);
    }

    @Bean
    @Primary
    public CommentService mockCommentService() {
        return Mockito.mock(CommentService.class);
    }

    @Bean
    @Primary
    public CommentController mockCommentController() {
        return Mockito.mock(CommentController.class);
    }
}
