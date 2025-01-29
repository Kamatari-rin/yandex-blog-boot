package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> postTestEndpoint() {
        return ResponseEntity.ok("PostController works!");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<PostDTO> posts = postService.getAllPosts(limit, offset);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid PostCreateDTO postCreateDTO) {
        PostDTO createdPost = postService.createPost(postCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateDTO postUpdateDTO) {
        postUpdateDTO.setPostId(postId);
        PostDTO updatedPost = postService.updatePost(postUpdateDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
