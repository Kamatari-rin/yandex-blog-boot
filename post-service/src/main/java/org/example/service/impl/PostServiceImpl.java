package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.enums.LikeTargetType;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.example.service.PostService;
import org.example.service.TagService;
import org.example.mapper.PostMapper;
import org.example.service.UserServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final PostMapper postMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        post.setTags(tagService.getTagsForPost(post.getId()));
        return enrichAndMap(post);
    }

    @Override
    public List<PostDTO> getAllPosts(int limit, int offset) {
        List<Post> posts = postRepository.findAllPosts(limit, offset);
        Set<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        Map<Long, Set<Tag>> tagsByPost = tagService.getTagsForPosts(postIds);
        posts.forEach(post ->
                post.setTags(tagsByPost.getOrDefault(post.getId(), Collections.emptySet()))
        );
        return posts.stream()
                .map(this::enrichAndMap)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO postCreateDTO) {
        Post post = postMapper.toEntity(postCreateDTO);
        Post savedPost = postRepository.save(post);

        if (postCreateDTO.getTags() != null && !postCreateDTO.getTags().isEmpty()) {
            Set<Tag> processedTags = processTags(postCreateDTO.getTags(), savedPost.getId());
            savedPost.setTags(processedTags);
        }
        return enrichAndMap(savedPost);
    }

    @Override
    @Transactional
    public PostDTO updatePost(PostUpdateDTO postUpdateDTO) {
        Post existingPost = postRepository.findPostById(postUpdateDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postUpdateDTO.getPostId()));

        existingPost.setTitle(postUpdateDTO.getTitle());
        existingPost.setContent(postUpdateDTO.getContent());
        existingPost.setImageUrl(postUpdateDTO.getImageUrl());
        existingPost.setUpdatedAt(Instant.now());

        postRepository.save(existingPost);

        if (postUpdateDTO.getTags() != null) {
            Set<Tag> processedTags = processTags(postUpdateDTO.getTags(), existingPost.getId());
            existingPost.setTags(processedTags);
        } else {
            existingPost.setTags(tagService.getTagsForPost(existingPost.getId()));
        }
        return enrichAndMap(existingPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deletePostById(postId);
    }

    @Override
    public List<PostDTO> getPostsByTag(String tagName) {
        List<Post> posts = postRepository.findPostsByTag(tagName);
        Set<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        Map<Long, Set<Tag>> tagsByPost = tagService.getTagsForPosts(postIds);

        return posts.stream().map(post -> {
            post.setTags(tagsByPost.getOrDefault(post.getId(), Collections.emptySet()));
            return enrichAndMap(post);
        }).collect(Collectors.toList());
    }

    private PostDTO enrichAndMap(Post post) {
        String authorName = userServiceClient.fetchUserById(post.getUserId()).getUsername();
        int likesCount = userServiceClient.fetchLikesCountByTarget(post.getId(), LikeTargetType.POST);
        int commentsCount = postRepository.countCommentsByPostId(post.getId());
        PostDTO dto = postMapper.toPostDTO(post);
        dto.setAuthorName(authorName);
        dto.setLikesCount(likesCount);
        dto.setCommentsCount(commentsCount);
        dto.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return dto;
    }

    private Set<Tag> processTags(Set<String> tagNames, Long postId) {
        Set<Tag> persistedTags = tagNames.stream()
                .map(tagService::findOrCreateTag)
                .collect(Collectors.toSet());
        tagService.deleteTagsForPost(postId);
        tagService.saveTagsForPost(postId, persistedTags);
        return persistedTags;
    }
}
