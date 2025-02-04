package org.example.config;

import org.example.mapper.LikeMapper;
import org.example.mapper.UserMapper;
import org.example.repository.LikeRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("org.example.service")
public class LikeServiceTestConfig {

    public LikeServiceTestConfig() {
        System.out.println("### LikeServiceTestConfig loaded ### ");
    }

    @Bean
    @Primary
    public LikeRepository mockLikeRepository() {
        return Mockito.mock(LikeRepository.class);
    }

    @Bean
    @Primary
    public LikeMapper mockLikeMapper() {
        return Mockito.mock(LikeMapper.class);
    }

    @Bean
    @Primary
    public UserMapper mockUserMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean
    @Primary
    public UserService mockUserService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public UserRepository mockUserRepository() {
        return Mockito.mock(UserRepository.class);
    }
}
