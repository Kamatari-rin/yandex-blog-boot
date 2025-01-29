package org.example.config;

import org.example.mapper.CommentMapper;
import org.example.mapper.CommentRowMapper;
import org.example.mapper.PostMapper;
import org.example.mapper.PostRowMapper;
import org.mapstruct.factory.Mappers;
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
    public PostMapper postMapper() {
        return Mappers.getMapper(PostMapper.class);
    }

    @Bean
    public CommentMapper commentMapper() {
        return Mappers.getMapper(CommentMapper.class);
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
