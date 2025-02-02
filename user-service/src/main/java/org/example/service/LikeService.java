package org.example.service;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;

public interface LikeService {

    LikeDTO saveOrUpdateLike(CreateLikeDTO createLikeDTO);
    int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType);
}
