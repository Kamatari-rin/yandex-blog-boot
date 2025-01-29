package org.example.repository;

import org.example.model.Post;


public interface PostRepository extends AbstractRepository<Post> {
    Post save(Post post);

    Post update(Post post);

    int countCommentsByPostId(Long postId);
}

