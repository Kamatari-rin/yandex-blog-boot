package org.example.repository;

import org.example.model.Post;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends AbstractRepository<Post> {
    Post save(Post post);

    Post update(Post post);

    int countCommentsByPostId(Long postId);

    List<Post> findAllPostsWithTags(int limit, int offset);

    List<Post> findPostsByTag(String tagName);

    Optional<Post> findPostWithTagsById(Long id);
}

