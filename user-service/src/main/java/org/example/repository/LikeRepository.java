package org.example.repository;

import org.example.enums.LikeTargetType;
import org.example.model.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends AbstractRepository<Like> {
    int countLikesByIdAndTarget(Long targetId, LikeTargetType targetType);

    Optional<Like> findByUserAndTarget(Long userId, Long targetId, LikeTargetType targetType);

    List<Like> findByUserIdAndTargetIdsAndType(Long userId, List<Long> targetIds, LikeTargetType targetType);

    Like saveOrUpdate(Like like);
}
