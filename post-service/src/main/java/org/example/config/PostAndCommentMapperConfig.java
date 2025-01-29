package org.example.config;

import org.example.mapper.CommentRowMapper;
import org.example.mapper.PostRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PostAndCommentMapperConfig {

    private final JdbcTemplate jdbcTemplate;

    public PostAndCommentMapperConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public PostRowMapper postRowMapper() {
        return new PostRowMapper(jdbcTemplate);
    }

    @Bean
    public CommentRowMapper commentRowMapper() {
        return new CommentRowMapper();
    }
}
