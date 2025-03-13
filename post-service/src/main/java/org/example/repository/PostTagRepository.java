package org.example.repository;

import org.example.model.PostTag;
import org.example.model.Tag;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface PostTagRepository extends CrudRepository<PostTag, Long>, PostTagRepositoryCustom {

    @Query("SELECT t.id, t.name FROM tags t " +
            "JOIN post_tags pt ON t.id = pt.tag_id " +
            "WHERE pt.post_id = :postId")
    Set<Tag> findTagsByPostId(Long postId);
    @Modifying
    @Query("DELETE FROM post_tags WHERE post_id = :postId")
    void deleteByPostId(Long postId);
}
