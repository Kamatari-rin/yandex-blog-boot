package org.example.junit.config;

import org.example.model.User;
import org.example.repository.impl.UserRepositoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.example.repository.UserRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@ComponentScan("org.example.repository")
public class UserRepositoryTestConfig {

    @Bean
    public JdbcTemplate mockJdbcTemplate() {
        return Mockito.mock(JdbcTemplate.class);
    }

    @Bean
    public RowMapper<User> mockUserRowMapper() {
        return Mockito.mock(RowMapper.class);
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate, RowMapper<User> userRowMapper) {
        return new UserRepositoryImpl(jdbcTemplate, userRowMapper);
    }
}
