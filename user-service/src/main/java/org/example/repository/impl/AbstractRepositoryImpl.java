package org.example.repository.impl;

import org.example.repository.AbstractRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepositoryImpl<T> implements AbstractRepository<T> {

    protected final JdbcTemplate jdbcTemplate;
    private final RowMapper<T> rowMapper;

    public AbstractRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public abstract String getTableName();

    @Override
    public Optional<T> findById(Long id) {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try {
            T result = jdbcTemplate.queryForObject(query, rowMapper, id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(int limit, int offset) {
        String query = "SELECT * FROM " + getTableName() + " ORDER BY id ASC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, rowMapper, limit, offset);
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        return jdbcTemplate.queryForObject(query, Integer.class);
    }
}
