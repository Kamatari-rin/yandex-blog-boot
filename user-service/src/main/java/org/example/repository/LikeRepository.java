package org.example.repository;

import org.example.enums.LikeTargetType;
import org.example.model.Like;

import java.util.Optional;

public interface LikeRepository extends AbstractRepository<Like> {
    int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType);

    Optional<Like> findByUserAndTarget(Long userId, Long targetId, LikeTargetType targetType);

    Like save(Like like);

    Like update(Like like);
}
