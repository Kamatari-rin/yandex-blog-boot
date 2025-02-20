package org.example.mapper;

import org.example.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");

        return Comment.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .postId(rs.getLong("post_id"))
                .userId(rs.getLong("user_id"))
                .parentCommentId(rs.getObject("parent_comment_id") != null ? rs.getLong("parent_comment_id") : null)
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(updatedAtTimestamp != null ? updatedAtTimestamp.toInstant() : null)
                .build();
    }
}
