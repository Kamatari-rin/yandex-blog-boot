package org.example.mapper;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.dto.UserDTO;
import org.example.model.Post;
import org.example.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "tags", expression = "java(mapTags(dto.getTags()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "tags", expression = "java(mapTags(dto.getTags()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostCreateDTO dto);

    @Mappings({
            @Mapping(source = "post.id", target = "id"),
            @Mapping(source = "post.userId", target = "userId"),
            @Mapping(source = "post.title", target = "title"),
            @Mapping(source = "post.imageUrl", target = "imageUrl"),
            @Mapping(source = "post.content", target = "content"),
            @Mapping(source = "post.authorName", target = "authorName"),
            @Mapping(source = "post.createdAt", target = "createdAt"),
            @Mapping(source = "post.updatedAt", target = "updatedAt"),
            @Mapping(target = "tags", source = "post.tags", qualifiedByName = "tagToStringSet"),
            @Mapping(target = "likesCount", expression = "java(getLikesCount(userDTO, post))"),
            @Mapping(target = "commentsCount", expression = "java(getCommentsCount(post))")
    })
    PostDTO toPostDTO(Post post, UserDTO userDTO);

    @Named("tagToStringSet")
    default Set<String> tagToStringSet(Set<Tag> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

    default int getLikesCount(UserDTO userDTO, Post post) {
        return 0;
    }

    default int getCommentsCount(Post post) {
        return 0;
    }

    default Set<Tag> mapTags(Set<String> tagNames) {
        if (tagNames == null) {
            return new HashSet<>();
        }
        return tagNames.stream()
                .map(tagName -> Tag.builder().name(tagName).build())
                .collect(Collectors.toSet());
    }
}

