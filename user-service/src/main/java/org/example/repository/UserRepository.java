package org.example.repository;

import org.example.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);
}
