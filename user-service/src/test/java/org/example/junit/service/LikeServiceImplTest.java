package org.example.junit.service;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.mapper.LikeMapper;
import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.example.service.LikeService;
import org.example.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LikeServiceImpl.class)
@ActiveProfiles("test")
public class LikeServiceImplTest {

    @Autowired
    private LikeService likeService;

    @MockBean
    private LikeRepository likeRepository;

    @MockBean
    private LikeMapper likeMapper;

    @BeforeEach
    void setUp() {
        reset(likeRepository);
    }

    private CreateLikeDTO createLikeDTO(Long userId, Long targetId, LikeTargetType targetType, boolean liked) {
        return CreateLikeDTO.builder()
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .liked(liked)
                .build();
    }

    private LikeDTO createLikeDTO(Long id, Long userId, Long targetId, LikeTargetType targetType, boolean liked) {
        return LikeDTO.builder()
                .id(id)
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .liked(liked)
                .build();
    }

    private Like createLike(Long id, Long userId, Long targetId, LikeTargetType targetType, boolean liked) {
        Like like = Like.builder()
                .id(id)
                .userId(userId)
                .targetId(targetId)
                .liked(liked)
                .build();
        like.setTargetType(targetType);
        return like;
    }

    @Test
    void testSaveOrUpdateLike_whenLikeDoesNotExist() {
        CreateLikeDTO createDto = createLikeDTO(1L, 2L, LikeTargetType.POST, true);
        Like likeEntity = createLike(null, 1L, 2L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = createLikeDTO(1L, 1L, 2L, LikeTargetType.POST, true);

        when(likeMapper.toEntity(createDto)).thenReturn(likeEntity);
        when(likeMapper.toDTO(likeEntity)).thenReturn(expectedLikeDTO);
        when(likeRepository.findByUserAndTarget(createDto.getUserId(), createDto.getTargetId(), createDto.getTargetType()))
                .thenReturn(Optional.empty());
        when(likeRepository.save(likeEntity)).thenReturn(likeEntity);

        LikeDTO result = likeService.saveOrUpdateLike(createDto);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(expectedLikeDTO, result, "Лайк, возвращаемый сервисом, не совпадает с ожидаемым");
    }

    @Test
    void testSaveOrUpdateLike_whenLikeExists() {
        CreateLikeDTO createDto = createLikeDTO(1L, 2L, LikeTargetType.POST, false);
        Like existingLike = createLike(1L, 1L, 2L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = createLikeDTO(1L, 1L, 2L, LikeTargetType.POST, false);

        when(likeMapper.toEntity(createDto)).thenReturn(existingLike);
        when(likeMapper.toDTO(existingLike)).thenReturn(expectedLikeDTO);
        when(likeRepository.findByUserAndTarget(createDto.getUserId(), createDto.getTargetId(), createDto.getTargetType()))
                .thenReturn(Optional.of(existingLike));

        existingLike.setLiked(createDto.isLiked());
        when(likeRepository.save(existingLike)).thenReturn(existingLike);

        LikeDTO result = likeService.saveOrUpdateLike(createDto);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(expectedLikeDTO, result, "Обновленный лайк не совпадает с ожидаемым");
    }

    @Test
    void testCountLikes() {
        Long targetId = 1L;
        LikeTargetType targetType = LikeTargetType.POST;
        int expectedCount = 5;

        when(likeRepository.countLikesByIdAndTarget(targetId, targetType))
                .thenReturn(expectedCount);

        int result = likeService.countLikesByIdAndTarget(targetId, targetType);

        assertEquals(expectedCount, result, "Количество лайков не совпадает с ожидаемым");
    }

    @Test
    void testGetLikesStatuses() {
        Long userId = 1L;
        List<Long> targetIds = List.of(10L, 11L);
        LikeTargetType targetType = LikeTargetType.POST;
        Like like1 = createLike(1L, userId, 10L, targetType, true);
        Like like2 = createLike(2L, userId, 11L, targetType, false);

        when(likeRepository.findByUserIdAndTargetIdsAndType(userId, targetIds, targetType))
                .thenReturn(List.of(like1, like2));

        List<?> statuses = likeService.getLikesStatuses(targetIds, userId, targetType);

        assertNotNull(statuses, "Статусы лайков не должны быть null");
        assertEquals(2, statuses.size(), "Размер списка статусов не совпадает");
    }
}
