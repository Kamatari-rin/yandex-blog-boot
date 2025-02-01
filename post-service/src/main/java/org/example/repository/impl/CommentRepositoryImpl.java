package org.example.repository.impl;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl extends AbstractRepositoryImpl<Comment> implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Comment> commentRowMapper;

    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Comment> commentRowMapper) {
        super(jdbcTemplate, commentRowMapper);
        this.jdbcTemplate = jdbcTemplate;
        this.commentRowMapper = commentRowMapper;
    }

    @Override
    public String getTableName() {
        return "post_service.comments";
    }

    @Override
    public Comment save(Comment comment) {
        String sql = "INSERT INTO " + getTableName() +
                " (content, post_id, user_id, parent_comment_id) " +
                "VALUES (?, ?, ?, ?) RETURNING *";

        return jdbcTemplate.queryForObject(sql, commentRowMapper,
                comment.getContent(),
                comment.getPostId(),
                comment.getUserId(),
                comment.getParentCommentId());
    }

    @Override
    public Comment update(Comment comment) {
        String sql = "UPDATE " + getTableName() +
                " SET content = ? " +
                "WHERE id = ? RETURNING *";

        return jdbcTemplate.queryForObject(sql, commentRowMapper,
                comment.getContent(),
                comment.getId());
    }

    @Override
    public List<Comment> findByPostId(Long postId, int limit, int offset) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE post_id = ? ORDER BY created_at ASC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, commentRowMapper, postId, limit, offset);
    }
}
