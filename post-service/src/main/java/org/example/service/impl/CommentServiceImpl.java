package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.dto.CommentUpdateDTO;
import org.example.mapper.CommentMapper;
import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден с id: " + commentId));
        return commentMapper.toCommentDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId, int limit, int offset) {
        return commentRepository.findByPostId(postId, limit, offset)
                .stream()
                .map(commentMapper::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentCreateDTO commentCreateDTO) {
        Comment comment = commentMapper.toEntity(commentCreateDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(savedComment);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(CommentUpdateDTO commentUpdateDTO) {
        Comment comment = commentMapper.toEntity(commentUpdateDTO);
        commentRepository.findById(comment.getId())
                .orElseThrow(() -> new RuntimeException("Комментарий не найден с id: " + commentUpdateDTO.getId()));
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

