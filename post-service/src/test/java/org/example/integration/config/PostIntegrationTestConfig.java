package org.example.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.controller.PostController;
import org.example.mapper.CommentRowMapper;
import org.example.mapper.PostMapper;
import org.example.mapper.PostRowMapper;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.example.repository.impl.PostRepositoryImpl;
import org.example.service.PostService;
import org.example.service.UserServiceClient;
import org.example.service.impl.PostServiceImpl;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(DataSourceConfiguration.class)
@ComponentScan(basePackages = "org.example.mapper")
public class PostIntegrationTestConfig {

    @Bean
    public MockMvc mockMvc(PostController postController) {
        return MockMvcBuilders.standaloneSetup(postController)
                .build();
    }

    @Bean
    public UserServiceClient userServiceClient() {
        return Mockito.mock(UserServiceClient.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }


    @Bean
    public PostRepository postRepository(JdbcTemplate jdbcTemplate, RowMapper<Post> postRowMapper) {
        return new PostRepositoryImpl(jdbcTemplate, postRowMapper);
    }

    @Bean
    public PostService postService(PostRepository postRepository, UserServiceClient userServiceClient, PostMapper postMapper) {
        return new PostServiceImpl(postRepository, userServiceClient, postMapper);
    }

    @Bean
    public PostController postController(PostService postService) {
        return new PostController(postService);
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public PostRowMapper postRowMapper(JdbcTemplate jdbcTemplate) {
        return new PostRowMapper(jdbcTemplate);
    }

    @Bean
    public CommentRowMapper commentRowMapper() {
        return new CommentRowMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
