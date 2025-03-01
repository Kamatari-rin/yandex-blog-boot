package org.example.integration.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@PropertySource("classpath:application-test.properties")
public class DataSourceConfiguration {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("password");

    static {
        postgresContainer.start();
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresContainer.getJdbcUrl() + "?currentSchema=post_service");
        config.setUsername(postgresContainer.getUsername());
        config.setPassword(postgresContainer.getPassword());
        config.setConnectionInitSql("SET search_path TO post_service");

        HikariDataSource dataSource = new HikariDataSource(config);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("test-schema.sql"));
        populator.setContinueOnError(false);
        populator.execute(dataSource);

        checkTablesCreated(dataSource);
        checkPostTagsConnections(dataSource);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private void checkTablesCreated(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT current_schema()")) {
            if (rs.next()) {
                System.out.println("Current schema: " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post_service.posts", Integer.class);
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post_service.tags", Integer.class);
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post_service.post_tags", Integer.class);
            System.out.println("Таблицы успешно созданы.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка: таблицы не были созданы в базе данных.", e);
        }
    }

    private void checkPostTagsConnections(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            String insertPostSql = "INSERT INTO post_service.posts (title, content, image_url, user_id) " +
                    "VALUES (?, ?, ?, ?) RETURNING id";

            Long postId = jdbcTemplate.queryForObject(insertPostSql, Long.class,
                    "Test Post",
                    "This is a test post content.",
                    "http://example.com/image.jpg",
                    1L);

            if (postId == null) {
                throw new RuntimeException("Ошибка: пост не был сохранен.");
            }

            String insertTagSql = "INSERT INTO post_service.tags (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
            jdbcTemplate.update(insertTagSql, "tag1");
            jdbcTemplate.update(insertTagSql, "tag2");

            String selectTagIdSql = "SELECT id FROM post_service.tags WHERE name = ?";
            Long tag1Id = jdbcTemplate.queryForObject(selectTagIdSql, Long.class, "tag1");
            Long tag2Id = jdbcTemplate.queryForObject(selectTagIdSql, Long.class, "tag2");

            String insertPostTagSql = "INSERT INTO post_service.post_tags (post_id, tag_id) VALUES (?, ?)";
            jdbcTemplate.update(insertPostTagSql, postId, tag1Id);
            jdbcTemplate.update(insertPostTagSql, postId, tag2Id);

            String sql = "SELECT COUNT(*) FROM post_service.post_tags WHERE post_id = ? AND tag_id = ?";
            Integer countTag1 = jdbcTemplate.queryForObject(sql, Integer.class, postId, tag1Id);
            Integer countTag2 = jdbcTemplate.queryForObject(sql, Integer.class, postId, tag2Id);

            if (countTag1 == 0 || countTag2 == 0) {
                throw new RuntimeException("Ошибка: связи между постами и тегами не созданы.");
            }

            System.out.println("Связи между постами и тегами успешно созданы.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке связей: " + e.getMessage(), e);
        }
    }
}