package org.example.service;

import org.example.dto.UserDTO;
import org.example.enums.LikeTargetType;

public interface UserServiceClient {
    UserDTO fetchUserById(Long userId);

    int fetchLikesCountByTarget(Long targetId, LikeTargetType targetType);
}