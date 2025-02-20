package org.example.repository;

import org.example.model.Comment;
import java.util.List;

public interface CommentRepository extends AbstractRepository<Comment> {
    Comment save(Comment comment);
    Comment update(Comment comment);
    List<Comment> findByPostId(Long postId, int limit, int offset);
}
