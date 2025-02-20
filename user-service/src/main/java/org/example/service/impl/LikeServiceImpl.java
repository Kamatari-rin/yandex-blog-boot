package org.example.service.impl;

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
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    public LikeServiceImpl(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType) {
        return likeRepository.countLikesByIdAndTarget(targetId, targetType);
    }

    @Override
    @Transactional
    public LikeDTO saveOrUpdateLike(CreateLikeDTO createLikeDTO) {
        Like like = likeMapper.toEntity(createLikeDTO);

        Optional<Like> existingLike = likeRepository.findByUserAndTarget(
                createLikeDTO.getUserId(), createLikeDTO.getTargetId(), createLikeDTO.getTargetType());

        Like savedLike = existingLike
                .map(l -> {
                    l.setLiked(createLikeDTO.isLiked());
                    return likeRepository.saveOrUpdate(l);
                })
                .orElseGet(() -> likeRepository.saveOrUpdate(like));

        return likeMapper.toDTO(savedLike);
    }

    @Override
    public List<LikeStatusDTO> getLikesStatuses(List<Long> targetIds, Long userId, LikeTargetType targetType) {
        List<Like> likes = likeRepository.findByUserIdAndTargetIdsAndType(userId, targetIds, targetType);

        return likes.stream()
                .map(like -> new LikeStatusDTO(like.getTargetId(), like.getTargetType(), like.isLiked()))
                .collect(Collectors.toList());
    }
}

