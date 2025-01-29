package org.example.config;

import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Like;
import org.example.model.User;
import org.example.repository.LikeRepository;
import org.example.repository.impl.LikeRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.util.DatabaseHealthCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.addDataSourceProperty("currentSchema", "user_service");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public UserRepositoryImpl userRepository(JdbcTemplate jdbcTemplate, RowMapper<User> userRowMapper) {
        return new UserRepositoryImpl(jdbcTemplate, userRowMapper);
    }

    @Bean
    public LikeRepositoryImpl likeRepository(JdbcTemplate jdbcTemplate, RowMapper<Like> likeRowMapper) {
        return new LikeRepositoryImpl(jdbcTemplate, likeRowMapper);
    }

    @Bean
    public DatabaseHealthCheck databaseHealthCheck(DataSource dataSource) {
        return new DatabaseHealthCheck(dataSource);
    }

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        try {
            DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            ClassPathResource schemaScript = new ClassPathResource("schema.sql");

            if (schemaScript.exists()) {
                populator.addScript(schemaScript);
                populator.execute(dataSource);
                System.out.println("Schema initialized successfully.");
            } else {
                System.out.println("schema.sql not found, skipping initialization.");
            }
        } catch (Exception e) {
            System.err.println("Error while initializing schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}