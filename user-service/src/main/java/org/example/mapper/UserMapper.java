package org.example.mapper;

import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "likedPosts", ignore = true)
    @Mapping(target = "dislikedPosts", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toEntity(CreateUserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "likedPosts", ignore = true)
    @Mapping(target = "dislikedPosts", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toEntity(UpdateUserDTO dto);

    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "user.email", target = "email"),
            @Mapping(source = "user.createdAt", target = "createdAt"),
            @Mapping(source = "user.updatedAt", target = "updatedAt"),
            @Mapping(source = "user.likedPosts", target = "likedPosts"),
            @Mapping(source = "user.dislikedPosts", target = "dislikedPosts")
    })
    UserDTO toUserDTO(User user);
}
