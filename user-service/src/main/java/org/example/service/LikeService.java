package org.example.service;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.dto.LikeStatusDTO;
import org.example.enums.LikeTargetType;

import java.util.List;

public interface LikeService {

    LikeDTO saveOrUpdateLike(CreateLikeDTO createLikeDTO);

    int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType);

    List<LikeStatusDTO> getLikesStatuses(List<Long> targetIds, Long userId, LikeTargetType targetType);
}

