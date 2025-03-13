package org.example.repository;

import org.example.model.Tag;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    @Query("SELECT * FROM tags WHERE name = :name")
    Optional<Tag> findByName(String name);
}
