package org.example.mapper;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.enums.LikeTargetType;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.example.service.UserServiceClient;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface PostMapper {

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "tags", source = "dto.tags")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "tags", source = "dto.tags")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostCreateDTO dto);

    @Mappings({
            @Mapping(source = "post.id", target = "id"),
            @Mapping(source = "post.userId", target = "userId"),
            @Mapping(source = "post.title", target = "title"),
            @Mapping(source = "post.imageUrl", target = "imageUrl"),
            @Mapping(source = "post.content", target = "content"),
            @Mapping(target = "authorName", expression = "java(getAuthorName(post, userServiceClient))"),
            @Mapping(source = "post.createdAt", target = "createdAt"),
            @Mapping(source = "post.updatedAt", target = "updatedAt"),
            @Mapping(target = "tags", source = "post.tags"),
            @Mapping(target = "likesCount", expression = "java(getLikesCount(post, userServiceClient))"),
            @Mapping(target = "commentsCount", expression = "java(getCommentsCount(post, postRepository))")
    })
    PostDTO toPostDTO(Post post, @Context UserServiceClient userServiceClient, @Context PostRepository postRepository);

    default int getLikesCount(Post post, @Context UserServiceClient userServiceClient) {
        return userServiceClient.fetchLikesCountByTarget(post.getId(), LikeTargetType.POST);
    }

    default int getCommentsCount(Post post, @Context PostRepository postRepository) {
        return postRepository.countCommentsByPostId(post.getId());
    }

    default String getAuthorName(Post post, @Context UserServiceClient userServiceClient) {
        return userServiceClient.fetchUserById(post.getUserId()).getUsername();
    }
}
