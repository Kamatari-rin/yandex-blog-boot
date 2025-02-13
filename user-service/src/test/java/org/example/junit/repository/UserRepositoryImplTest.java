package org.example.junit.repository;

import org.example.junit.config.UserRepositoryTestConfig;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserRepositoryTestConfig.class)
@ActiveProfiles("test")
public class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RowMapper<User> userRowMapper;

    private User createUser(Long id, String username, String email, String password) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    private void assertUserEquals(User expected, User actual) {
        assertNotNull(actual, "Результат не должен быть null");
        assertEquals(expected.getId(), actual.getId(), "ID пользователя не совпадает");
        assertEquals(expected.getUsername(), actual.getUsername(), "Имя пользователя не совпадает");
        assertEquals(expected.getEmail(), actual.getEmail(), "Email пользователя не совпадает");
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt(), "Время создания пользователя не совпадает");
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt(), "Время обновления пользователя не совпадает");
    }

    @Test
    void testFindUserById() {
        Long userId = 1L;
        User expectedUser = createUser(userId, "testUser", "test@example.com", "password");

        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper), eq(userId))).thenReturn(expectedUser);

        Optional<User> result = userRepository.findById(userId);
        assertTrue(result.isPresent(), "Пользователь должен быть найден");
        assertUserEquals(expectedUser, result.get());
    }

    @Test
    void testSaveUser() throws SQLException {
        User newUser = createUser(2L, "newUser", "newuser@example.com", "password123");

        when(userRowMapper.mapRow(any(), anyInt())).thenReturn(newUser);

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(userRowMapper),
                eq(newUser.getUsername()),
                eq(newUser.getEmail()),
                eq(newUser.getPassword())
        )).thenReturn(newUser);

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser, "Сохраненный пользователь не должен быть null");
        assertUserEquals(newUser, savedUser);
    }

    @Test
    void testUpdateUser() {
        User updatedUser = createUser(1L, "updatedUser", "updated@example.com", "password123");

        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper),
                eq(updatedUser.getUsername()),
                eq(updatedUser.getEmail()),
                eq(updatedUser.getPassword()),
                eq(Timestamp.from(updatedUser.getUpdatedAt())),
                eq(updatedUser.getId())))
                .thenReturn(updatedUser);

        User result = userRepository.update(updatedUser);
        assertUserEquals(updatedUser, result);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            String sql = (String) args[0];
            Long id = (Long) args[1];

            System.out.println("SQL: " + sql);
            System.out.println("User ID: " + id);

            return 1;
        }).when(jdbcTemplate).update(anyString(), eq(userId));

        userRepository.delete(userId);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(userId));
    }
}

