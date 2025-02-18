package org.example.mapper;

import org.example.model.Post;
import org.example.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PostRowMapperWithTags implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long postId = rs.getLong("id");

        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");

        // Читаем агрегированные теги, если они есть
        String tagsStr = rs.getString("tags");
        Set<Tag> tags = new HashSet<>();
        if (tagsStr != null && !tagsStr.trim().isEmpty()) {
            tags = Arrays.stream(tagsStr.split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .map(tagName -> new Tag(null, tagName))
                    .collect(Collectors.toSet());
        }

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
}