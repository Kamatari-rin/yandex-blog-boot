package org.example.integration.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostTestRepositoryUtil {

    private final JdbcTemplate jdbcTemplate;

    public PostTestRepositoryUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteAllPosts() {
        jdbcTemplate.update("TRUNCATE TABLE post_service.post_tags RESTART IDENTITY CASCADE");
        jdbcTemplate.update("TRUNCATE TABLE post_service.posts RESTART IDENTITY CASCADE");
        jdbcTemplate.update("TRUNCATE TABLE post_service.tags RESTART IDENTITY CASCADE");
    }
}
