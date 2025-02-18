package org.example.config;

import org.example.mapper.CommentRowMapper;
import org.example.mapper.PostRowMapper;
import org.example.mapper.PostRowMapperWithTags;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MapperConfig {

    private final JdbcTemplate jdbcTemplate;

    public MapperConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public PostRowMapper postRowMapper() {
        return new PostRowMapper(jdbcTemplate);
    }

    @Bean
    public PostRowMapperWithTags postRowMapperWithTags() {
        return new PostRowMapperWithTags();
    }

    @Bean
    public CommentRowMapper commentRowMapper() {
        return new CommentRowMapper();
    }
}
