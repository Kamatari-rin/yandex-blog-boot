package org.example.mapper;

import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.dto.CommentUpdateDTO;
import org.example.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentCreateDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentUpdateDTO dto);

    @Mappings({
            @Mapping(source = "comment.id", target = "id"),
            @Mapping(source = "comment.content", target = "content"),
            @Mapping(source = "comment.userId", target = "userId"),
            @Mapping(source = "comment.postId", target = "postId"),
            @Mapping(source = "comment.parentCommentId", target = "parentCommentId"),
            @Mapping(source = "comment.createdAt", target = "createdAt"),
            @Mapping(source = "comment.updatedAt", target = "updatedAt"),
            @Mapping(target = "userName", expression = "java(getUserName(comment.getUserId()))")
    })
    CommentDTO toCommentDTO(Comment comment);


    default String getUserName(Long userId) {
        return "User_" + userId;
    }
}

