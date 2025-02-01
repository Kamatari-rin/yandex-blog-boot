package org.example.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.example.mapper.CommentMapper;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.example.repository.impl.CommentRepositoryImpl;
import org.example.service.CommentService;
import org.example.service.PostService;
import org.example.service.UserServiceClient;
import org.example.service.impl.CommentServiceImpl;
import org.example.service.impl.PostServiceImpl;
import org.example.service.impl.UserServiceClientImpl;
import org.springframework.context.annotation.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
@Import({
        DatabaseConfig.class,
        JacksonConfig.class,
        MapperConfig.class
})
public class PostServiceAppConfig {

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

    @Bean
    public RestTemplate restTemplate() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(5))
                .setResponseTimeout(Timeout.ofSeconds(5))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }


    @Bean
    public UserServiceClient userServiceClient(RestTemplate restTemplate) {
        return new UserServiceClientImpl(restTemplate);
    }

    @Bean
    public PostService postService(PostRepository postRepository, UserServiceClient userServiceClient, PostMapper postMapper) {
        return new PostServiceImpl(postRepository, userServiceClient, postMapper);
    }


    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
