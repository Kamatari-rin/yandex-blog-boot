package org.example.config;

import org.example.mapper.LikeRowMapper;
import org.example.mapper.UserRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserAndLikeMapperConfig {

    @Bean
    public UserRowMapper userRowMapper() {
        return new UserRowMapper();
    }

    @Bean
    public LikeRowMapper likeRowMapper() {
        return new LikeRowMapper();
    }
}
