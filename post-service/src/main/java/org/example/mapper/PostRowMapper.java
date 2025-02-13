package org.example.mapper;

import org.example.model.Post;
import org.example.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class PostRowMapper implements RowMapper<Post> {

    public PostRowMapper(JdbcTemplate jdbcTemplate) {
    }

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long postId = rs.getLong("id");

        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");

        return Post.builder()
                .id(postId)
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .imageUrl(rs.getString("image_url"))
                .userId(rs.getLong("user_id"))
                .createdAt(createdAtTimestamp != null ? createdAtTimestamp.toInstant() : null)
                .updatedAt(updatedAtTimestamp != null ? updatedAtTimestamp.toInstant() : null)
                .tags(new HashSet<>())
                .build();
    }
}
