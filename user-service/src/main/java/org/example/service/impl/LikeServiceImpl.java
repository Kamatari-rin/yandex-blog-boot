package org.example.service.impl;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.mapper.LikeMapper;
import org.example.model.Like;
import org.example.repository.LikeRepository;
import org.example.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public LikeDTO addLike(CreateLikeDTO createLikeDTO) {
        Like like = likeMapper.toEntity(createLikeDTO);

        Optional<Like> existingLike = likeRepository.findByUserAndTarget(
                createLikeDTO.getUserId(), createLikeDTO.getTargetId(), createLikeDTO.getTargetType());

        if (existingLike.isPresent()) {
            throw new RuntimeException("User already liked this target.");
        }

        Like savedLike = likeRepository.save(like);
        return new LikeDTO(savedLike.getId(), savedLike.getUserId(), savedLike.getTargetId(), savedLike.getTargetType(), savedLike.isLiked());
    }

    @Override
    @Transactional
    public LikeDTO updateLike(Long id, boolean isLiked) {
        Like like = likeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Like not found with id: " + id));

        like.setLiked(isLiked);
        Like updatedLike = likeRepository.update(like);
        return new LikeDTO(updatedLike.getId(), updatedLike.getUserId(), updatedLike.getTargetId(), updatedLike.getTargetType(), updatedLike.isLiked());
    }

    @Override
    @Transactional
    public void removeLike(Long id) {
        likeRepository.delete(id);
    }
}

