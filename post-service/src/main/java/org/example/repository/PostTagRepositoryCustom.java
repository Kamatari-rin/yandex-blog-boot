package org.example.repository;

import org.example.model.Tag;
import java.util.Map;
import java.util.Set;

public interface PostTagRepositoryCustom {
    Map<Long, Set<Tag>> findTagsByPostIds(Set<Long> postIds);
}