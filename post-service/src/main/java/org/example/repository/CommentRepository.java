package org.example.repository;

import org.example.model.Comment;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT * FROM comments WHERE post_id = :postId ORDER BY created_at ASC LIMIT :limit OFFSET :offset")
    List<Comment> findByPostId(@Param("postId") Long postId, @Param("limit") int limit, @Param("offset") int offset);
}