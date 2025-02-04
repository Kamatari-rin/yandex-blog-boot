package org.example.service;

import org.example.config.LikeServiceTestConfig;
import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.mapper.LikeMapper;
import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LikeServiceTestConfig.class)
@ActiveProfiles("test")
public class LikeServiceImplTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeMapper likeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        return Like.builder()
                .id(id)
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .liked(liked)
                .build();
    }

    @Test
    void testSaveOrUpdateLike_whenLikeDoesNotExist() {
        CreateLikeDTO createLikeDTO = createLikeDTO(1L, 2L, LikeTargetType.POST, true);
        Like likeEntity = createLike(1L, 2L, 2L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = createLikeDTO(1L, 2L, 2L, LikeTargetType.POST, true);

        when(likeMapper.toEntity(createLikeDTO)).thenReturn(likeEntity);
        when(likeMapper.toDTO(likeEntity)).thenReturn(expectedLikeDTO);

        when(likeRepository.findByUserAndTarget(createLikeDTO.getUserId(), createLikeDTO.getTargetId(), createLikeDTO.getTargetType()))
                .thenReturn(Optional.empty());

        when(likeRepository.saveOrUpdate(likeEntity)).thenReturn(likeEntity);

        LikeDTO result = likeService.saveOrUpdateLike(createLikeDTO);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(expectedLikeDTO, result, "Лайк, возвращаемый сервисом, не совпадает с ожидаемым");
    }

    @Test
    void testSaveOrUpdateLike_whenLikeExists() {
        CreateLikeDTO createLikeDTO = createLikeDTO(1L, 2L, LikeTargetType.POST, false);
        Like existingLike = createLike(1L, 2L, 2L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = createLikeDTO(1L, 2L, 2L, LikeTargetType.POST, false);

        when(likeMapper.toEntity(createLikeDTO)).thenReturn(existingLike);
        when(likeMapper.toDTO(existingLike)).thenReturn(expectedLikeDTO);

        when(likeRepository.findByUserAndTarget(createLikeDTO.getUserId(), createLikeDTO.getTargetId(), createLikeDTO.getTargetType()))
                .thenReturn(Optional.of(existingLike));

        when(likeRepository.saveOrUpdate(existingLike)).thenReturn(existingLike);

        LikeDTO result = likeService.saveOrUpdateLike(createLikeDTO);

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(expectedLikeDTO, result, "Обновленный лайк не совпадает с ожидаемым");
    }
}
