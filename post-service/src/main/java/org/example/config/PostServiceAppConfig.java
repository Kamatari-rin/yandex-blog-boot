package org.example.config;

import org.example.controller.PostTestController;
import org.springframework.context.annotation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
public class PostServiceAppConfig {

    @Import(DatabaseConfig.class)
    public static class DatabaseConfigImport {
    }

    @Import(JacksonConfig.class)
    public static class JacksonConfigImport {

    }


    @Bean
    public PostTestController postTestController() {
        return new PostTestController();
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
