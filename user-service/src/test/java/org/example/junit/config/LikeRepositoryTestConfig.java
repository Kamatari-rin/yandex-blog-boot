package org.example.junit.config;

import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.example.repository.impl.LikeRepositoryImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@ComponentScan("org.example.repository")
public class LikeRepositoryTestConfig {

    @Bean
    @Primary
    public JdbcTemplate mockJdbcTemplate() {
        return Mockito.mock(JdbcTemplate.class);
    }

    @Bean
    @Primary
    public RowMapper<Like> mockLikeRowMapper() {
        return Mockito.mock(RowMapper.class);
    }

    @Bean
    @Primary
    public LikeRepository likeRepository(JdbcTemplate jdbcTemplate, RowMapper<Like> likeRowMapper) {
        return new LikeRepositoryImpl(jdbcTemplate, likeRowMapper);
    }
}
