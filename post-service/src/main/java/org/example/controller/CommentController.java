package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.dto.CommentUpdateDTO;
import org.example.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(
            @RequestParam Long postId,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId, limit, offset);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO) {
        CommentDTO createdComment = commentService.createComment(commentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateDTO commentUpdateDTO) {
        commentUpdateDTO.setId(commentId);
        CommentDTO updatedComment = commentService.updateComment(commentUpdateDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
