package org.example.service;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;

import java.util.List;

public interface PostService {
    PostDTO getPostById(Long postId);

    List<PostDTO> getAllPosts(int limit, int offset);

    PostDTO createPost(PostCreateDTO postCreateDTO);

    PostDTO updatePost(PostUpdateDTO postUpdateDTO);

    void deletePost(Long postId);

    List<PostDTO> getPostsByTag(String tagName, int limit, int offset);
}