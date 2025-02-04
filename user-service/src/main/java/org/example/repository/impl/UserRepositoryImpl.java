package org.example.repository.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;

public class UserRepositoryImpl extends AbstractRepositoryImpl<User> implements UserRepository {
    private final RowMapper<User> userRowMapper;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<User> userRowMapper) {
        super(jdbcTemplate, userRowMapper);
        this.userRowMapper = userRowMapper;
    }

    @Override
    public String getTableName() {
        return "users";
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO " + getTableName() +
                " (username, email, password) VALUES (?, ?, ?) RETURNING *";

        return jdbcTemplate.queryForObject(sql, userRowMapper,
                user.getUsername(),
                user.getEmail(),
                user.getPassword());
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE " + getTableName() +
                " SET username = ?, email = ?, password = ?, updated_at = ? WHERE id = ? RETURNING *";

        return jdbcTemplate.queryForObject(sql, userRowMapper,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Timestamp.from(user.getUpdatedAt()),
                user.getId());
    }
}
