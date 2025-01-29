package org.example.mapper;

import org.example.enums.LikeTargetType;
import org.example.model.Like;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Like.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .targetId(rs.getLong("target_id"))
                .targetType(LikeTargetType.fromId(rs.getInt("target_type_id")))
                .isLiked(rs.getBoolean("is_liked"))
                .build();
    }
}
