package org.example.config;

import org.example.mapper.LikeMapper;
import org.example.mapper.UserMapper;
import org.example.repository.LikeRepository;
import org.example.repository.UserRepository;
import org.example.service.LikeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("org.example.service")
public class UserServiceTestConfig {

    public UserServiceTestConfig() {
        System.out.println("### TestConfig loaded ### ");
    }

    @Bean
    @Primary
    public UserRepository mockUserRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public UserMapper mockUserMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean
    @Primary
    public LikeMapper mockLikeMapper() {
        return Mockito.mock(LikeMapper.class);
    }

    @Bean
    @Primary
    public LikeService mockLikeService() {
        return Mockito.mock(LikeService.class);
    }

    @Bean
    @Primary
    public LikeRepository mockLikeRepository() {
        return Mockito.mock(LikeRepository.class);
    }
}
