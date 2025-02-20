package org.example.junit.repository;

import org.example.junit.config.LikeRepositoryTestConfig;
import org.example.enums.LikeTargetType;
import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LikeRepositoryTestConfig.class)
public class LikeRepositoryImplTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Like> likeRowMapper;

    private final Long userId = 1L;
    private final Long targetId = 1L;
    private final LikeTargetType targetType = LikeTargetType.POST;

    @BeforeEach
    void setUp() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(targetId), eq(targetType.getId())))
                .thenReturn(5);
    }

    private Like createLike(Long userId, Long targetId, LikeTargetType targetType, boolean liked) {
        return Like.builder()
                .id(1L)
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .liked(liked)
                .build();
    }

    private void mockQueryForObject(Like like) {
        when(jdbcTemplate.queryForObject(anyString(), eq(likeRowMapper), eq(userId), eq(targetId), eq(targetType.getId())))
                .thenReturn(like);
    }

    @Test
    void testCountLikesByIdAndTarget() {
        int result = likeRepository.countLikesByIdAndTarget(targetId, targetType);

        assertEquals(5, result, "Количество лайков не совпадает с ожидаемым");
    }

    @Test
    void testFindByUserAndTarget_whenLikeExists() {
        Like expectedLike = createLike(userId, targetId, targetType, true);
        mockQueryForObject(expectedLike);

        Optional<Like> result = likeRepository.findByUserAndTarget(userId, targetId, targetType);

        assertTrue(result.isPresent(), "Лайк должен быть найден");
        assertEquals(expectedLike, result.get(), "Найденный лайк не совпадает с ожидаемым");
    }

    @Test
    void testFindByUserAndTarget_whenLikeDoesNotExist() {
        when(jdbcTemplate.queryForObject(anyString(), eq(likeRowMapper), eq(userId), eq(targetId), eq(targetType.getId())))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<Like> result = likeRepository.findByUserAndTarget(userId, targetId, targetType);

        assertFalse(result.isPresent(), "Лайк не должен быть найден");
    }

    @Test
    void testSaveOrUpdate() {
        Like likeToSave = createLike(userId, targetId, targetType, true);
        Like savedLike = createLike(userId, targetId, targetType, true);

        when(jdbcTemplate.queryForObject(anyString(),
                eq(likeRowMapper),
                eq(likeToSave.getUserId()),
                eq(likeToSave.getTargetId()),
                eq(likeToSave.getTargetType().getId()),
                eq(likeToSave.isLiked()))).thenReturn(savedLike);

        Like result = likeRepository.saveOrUpdate(likeToSave);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(savedLike, result, "Сохраненный лайк не совпадает с ожидаемым");
    }
}

