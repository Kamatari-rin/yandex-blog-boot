package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.dto.LikeStatusDTO;
import org.example.enums.LikeTargetType;
import org.example.mapper.LikeMapper;
import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.example.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Override
    @Transactional(readOnly = true)
    public int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType) {
        return likeRepository.countLikesByIdAndTarget(targetId, targetType);
    }

    @Override
    @Transactional
    public LikeDTO saveOrUpdateLike(CreateLikeDTO createLikeDTO) {
        Like like = likeMapper.toEntity(createLikeDTO);

        Optional<Like> existingLike = likeRepository.findByUserAndTarget(
                createLikeDTO.getUserId(),
                createLikeDTO.getTargetId(),
                createLikeDTO.getTargetType()
        );

        Like savedLike = existingLike
                .map(existing -> {
                    existing.setLiked(createLikeDTO.isLiked());
                    return likeRepository.save(existing);
                })
                .orElseGet(() -> likeRepository.save(like));

        return likeMapper.toDTO(savedLike);
    }


    @Override
    @Transactional(readOnly = true)
    public List<LikeStatusDTO> getLikesStatuses(List<Long> targetIds, Long userId, LikeTargetType targetType) {
        List<Like> likes = likeRepository.findByUserIdAndTargetIdsAndType(userId, targetIds, targetType);
        return likes.stream()
                .map(like -> new LikeStatusDTO(like.getTargetId(), like.getTargetType(), like.isLiked()))
                .collect(Collectors.toList());
    }
}


