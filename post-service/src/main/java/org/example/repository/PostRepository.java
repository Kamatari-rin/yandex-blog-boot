package org.example.repository;

import org.example.model.Post;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT * FROM posts ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<Post> findAllPosts(int limit, int offset);

    @Query("SELECT * FROM posts WHERE id = :id")
    Optional<Post> findPostById(Long id);

    @Query("""
           SELECT DISTINCT p.* 
           FROM posts p 
           JOIN post_tags pt ON p.id = pt.post_id 
           JOIN tags t ON pt.tag_id = t.id 
           WHERE t.name ILIKE :tag
           ORDER BY p.created_at DESC
           """)
    List<Post> findPostsByTag(@Param("tag") String tag);

    @Query("SELECT COUNT(*) FROM comments WHERE post_id = :postId")
    int countCommentsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM posts WHERE id = :id")
    void deletePostById(@Param("id") Long id);
}

