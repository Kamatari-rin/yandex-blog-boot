package org.example.mapper;

import org.example.dto.CreateLikeDTO;
import org.example.enums.LikeTargetType;
import org.example.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "id", ignore = true)
    Like toEntity(CreateLikeDTO dto);

    default LikeTargetType mapTargetType(int targetTypeId) {
        return LikeTargetType.fromId(targetTypeId);
    }
}
