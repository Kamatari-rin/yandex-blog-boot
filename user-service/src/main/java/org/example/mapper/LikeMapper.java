package org.example.mapper;

import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "targetTypeValue", expression = "java(dto.getTargetType().getId())")
    Like toEntity(CreateLikeDTO dto);

    LikeDTO toDTO(Like like);

    default LikeTargetType mapTargetType(int targetTypeId) {
        return LikeTargetType.fromId(targetTypeId);
    }
}
