package org.example.repository;

import org.example.enums.LikeTargetType;
import org.example.model.Like;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {

    @Query("SELECT COUNT(*) FROM likes WHERE target_id = :targetId AND target_type_id = CAST(:#{#targetType.getId()} AS INTEGER) AND is_liked = true")
    int countLikesByIdAndTarget(@Param("targetId") Long targetId, @Param("targetType") LikeTargetType targetType);

    @Query("SELECT * FROM likes WHERE user_id = :userId AND target_id = :targetId AND target_type_id = CAST(:#{#targetType.getId()} AS INTEGER)")
    Optional<Like> findByUserAndTarget(@Param("userId") Long userId,
                                       @Param("targetId") Long targetId,
                                       @Param("targetType") LikeTargetType targetType);

    @Query("SELECT * FROM likes WHERE user_id = :userId AND target_id IN (:targetIds) AND target_type_id = CAST(:#{#targetType.getId()} AS INTEGER)")
    List<Like> findByUserIdAndTargetIdsAndType(@Param("userId") Long userId,
                                               @Param("targetIds") List<Long> targetIds,
                                               @Param("targetType") LikeTargetType targetType);
}

