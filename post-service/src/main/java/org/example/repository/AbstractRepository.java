package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {

    Optional<T> findById(Long id);

    List<T> findAll(int limit, int offset);

    void delete(Long id);

    int count();
}