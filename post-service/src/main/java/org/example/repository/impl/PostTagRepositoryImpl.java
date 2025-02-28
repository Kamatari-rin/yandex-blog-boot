package org.example.repository.impl;

import org.example.model.Tag;
import org.example.repository.PostTagRepositoryCustom;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PostTagRepositoryImpl implements PostTagRepositoryCustom {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostTagRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Map<Long, Set<Tag>> findTagsByPostIds(Set<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }
        String sql = """
            SELECT pt.post_id, t.id AS tag_id, t.name AS tag_name
            FROM post_tags pt
            JOIN tags t ON pt.tag_id = t.id
            WHERE pt.post_id IN (:postIds)
        """;
        Map<String, Object> params = Collections.singletonMap("postIds", postIds);
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, params);
        return rows.stream().collect(Collectors.groupingBy(
                row -> ((Number) row.get("post_id")).longValue(),
                Collectors.mapping(
                        row -> Tag.builder()
                                .id(((Number) row.get("tag_id")).longValue())
                                .name((String) row.get("tag_name"))
                                .build(),
                        Collectors.toSet()
                )
        ));
    }
}
