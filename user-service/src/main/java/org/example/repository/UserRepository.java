package org.example.repository;

import org.example.model.User;

public interface UserRepository extends AbstractRepository<User> {

    User save(User user);

    User update(User user);
}
