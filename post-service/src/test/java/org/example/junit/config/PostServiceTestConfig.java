package org.example.junit.config;

import org.example.controller.CommentController;
import org.example.mapper.CommentMapper;
import org.example.mapper.PostMapper;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.service.CommentService;
import org.example.service.PostService;
import org.example.service.UserServiceClient;
import org.example.service.impl.PostServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("org.example.service")
public class PostServiceTestConfig {

    @Bean
    @Primary
    public PostRepository mockPostRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    @Primary
    public CommentService mockCommentService() {
        return Mockito.mock(CommentService.class);
    }

    @Bean
    @Primary
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    @Primary
    public CommentMapper mockCommentMapper() {
        return Mockito.mock(CommentMapper.class);
    }

    @Bean
    @Primary
    public CommentController mockCommentController() {
        return Mockito.mock(CommentController.class);
    }

    @Bean
    @Primary
    public PostMapper mockPostMapper() {
        return Mockito.mock(PostMapper.class);
    }

    @Bean
    @Primary
    public UserServiceClient mockUserServiceClient() {
        return Mockito.mock(UserServiceClient.class);
    }

    @Bean
    @Primary
    public PostService postService(PostRepository postRepository, UserServiceClient userServiceClient, PostMapper postMapper) {
        return new PostServiceImpl(postRepository, userServiceClient, postMapper);
    }
}
