package org.example.config;


import org.example.controller.UserTestController;
import org.springframework.context.annotation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
public class UserServiceAppConfig {

    @Import(DatabaseConfig.class)
    public static class DatabaseConfigImport {
    }

    @Import(JacksonConfig.class)
    public static class JacksonConfigImport {

    }

    @Bean
    public UserTestController userTestController() {
        return new UserTestController();
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
