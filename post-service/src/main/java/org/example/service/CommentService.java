package org.example.service;

import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.dto.CommentUpdateDTO;

import java.util.List;

public interface CommentService {
    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getCommentsByPostId(Long postId, int limit, int offset);
    CommentDTO createComment(CommentCreateDTO commentCreateDTO);
    CommentDTO updateComment(CommentUpdateDTO commentUpdateDTO);
    void deleteComment(Long commentId);
}
