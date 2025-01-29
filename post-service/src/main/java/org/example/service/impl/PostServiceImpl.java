package org.example.service.impl;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.example.service.PostService;
import org.example.service.UserServiceClient;
import org.example.mapper.PostMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceClient userServiceClient;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, UserServiceClient userServiceClient, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userServiceClient = userServiceClient;
        this.postMapper = postMapper;
    }

    @Override
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        return postMapper.toPostDTO(post, userServiceClient, postRepository);
    }

    @Override
    public List<PostDTO> getAllPosts(int limit, int offset) {
        return postRepository.findAll(limit, offset)
                .stream()
                .map(post -> postMapper.toPostDTO(post, userServiceClient, postRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO postCreateDTO) {
        Post post = postMapper.toEntity(postCreateDTO);
        Post savedPost = postRepository.save(post);
        return postMapper.toPostDTO(savedPost, userServiceClient, postRepository);
    }

    @Override
    @Transactional
    public PostDTO updatePost(PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postUpdateDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postUpdateDTO.getPostId()));

        postMapper.toEntity(postUpdateDTO);
        postRepository.update(post);

        return postMapper.toPostDTO(post, userServiceClient, postRepository);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.delete(postId);
    }
}
