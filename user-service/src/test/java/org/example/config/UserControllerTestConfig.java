package org.example.config;

import org.example.controller.LikeController;
import org.example.dto.UserDTO;
import org.example.mapper.LikeMapper;
import org.example.mapper.UserMapper;
import org.example.repository.LikeRepository;
import org.example.service.LikeService;
import org.example.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("org.example.controller")
public class UserControllerTestConfig {

    @Bean
    @Primary
    public UserService mockUserService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public LikeController mockLikeController() {
        return Mockito.mock(LikeController.class);
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
