package org.example.service;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;

public interface LikeService {
    int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType);
    LikeDTO addLike(CreateLikeDTO createLikeDTO);
    LikeDTO updateLike(Long id, boolean isLiked);
    void removeLike(Long id);
}
