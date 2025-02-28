package org.example.mapper;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostUpdateDTO dto);

    @Mappings({
            @Mapping(source = "post.id", target = "id"),
            @Mapping(source = "post.userId", target = "userId"),
            @Mapping(source = "post.title", target = "title"),
            @Mapping(source = "post.content", target = "content"),
            @Mapping(source = "post.imageUrl", target = "imageUrl"),
            @Mapping(source = "post.createdAt", target = "createdAt"),
            @Mapping(source = "post.updatedAt", target = "updatedAt"),
            @Mapping(target = "authorName", ignore = true),
            @Mapping(target = "commentsCount", ignore = true),
            @Mapping(target = "likesCount", ignore = true),
            @Mapping(target = "tags", source = "post.tags")
    })
    PostDTO toPostDTO(Post post);
}