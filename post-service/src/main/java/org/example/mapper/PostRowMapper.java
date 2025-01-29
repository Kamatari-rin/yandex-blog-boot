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
    private final JdbcTemplate jdbcTemplate;

    public PostRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long postId = rs.getLong("id");

        Set<Tag> tags = getTagsByPostId(postId);

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
                .tags(tags)
                .build();
    }

    private Set<Tag> getTagsByPostId(Long postId) {
        String sql = "SELECT t.id, t.name FROM tags t " +
                "JOIN post_tags pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, new TagRowMapper(), postId));
    }
}
