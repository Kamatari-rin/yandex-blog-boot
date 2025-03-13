package org.example.repository;

import org.example.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM users ORDER BY id ASC LIMIT :limit OFFSET :offset")
    List<User> findAllUsers(int limit, int offset);
}
