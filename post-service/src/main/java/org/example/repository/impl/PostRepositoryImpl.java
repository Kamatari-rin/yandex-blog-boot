package org.example.repository.impl;

import org.example.model.Post;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class PostRepositoryImpl extends AbstractRepositoryImpl<Post> implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> postRowMapper;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Post> postRowMapper) {
        super(jdbcTemplate, postRowMapper);
        this.jdbcTemplate = jdbcTemplate;
        this.postRowMapper = postRowMapper;
    }

    @Override
    public String getTableName() {
        return "post_service.posts";
    }

    @Override
    public Post save(Post post) {
        String sql = "INSERT INTO " + getTableName() +
                " (title, content, image_url, user_id) " +
                "VALUES (?, ?, ?, ?) RETURNING *";

        Post savedPost = jdbcTemplate.queryForObject(sql, postRowMapper,
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getUserId());

        if (savedPost != null && post.getTags() != null) {
            saveTagsForPost(savedPost.getId(), post.getTags());
        }

        return savedPost;
    }

    @Override
    public Post update(Post post) {
        String sql = "UPDATE " + getTableName() +
                " SET title = ?, content = ?, image_url = ? " +
                "WHERE id = ? RETURNING *";

        Post updatedPost = jdbcTemplate.queryForObject(sql, postRowMapper,
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getId());

        if (updatedPost != null) {
            deleteTagsForPost(updatedPost.getId());
            saveTagsForPost(updatedPost.getId(), post.getTags());
        }

        return updatedPost;
    }

    @Override
    public int countCommentsByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }

    @Override
    public List<Post> findPostsByTag(String tagName) {
        String sql = """
        SELECT 
            p.id, 
            p.title, 
            p.content, 
            p.image_url, 
            p.user_id, 
            p.created_at, 
            p.updated_at,
            STRING_AGG(t.name, ',') AS tags
        FROM posts p
        JOIN post_tags pt ON p.id = pt.post_id
        JOIN tags t ON pt.tag_id = t.id
        WHERE t.name ILIKE ?
        GROUP BY p.id
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setImageUrl(rs.getString("image_url"));
            post.setUserId(rs.getLong("user_id"));
            post.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            post.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());

            String tagString = rs.getString("tags");
            if (tagString != null) {
                Set<Tag> tags = Arrays.stream(tagString.split(","))
                        .map(tag -> new Tag(null, tag.trim()))
                        .collect(Collectors.toSet());
                post.setTags(tags);
            }

            return post;
        }, "%" + tagName + "%"); // Добавляем "%" для частичного поиска
    }

    private void deleteTagsForPost(Long postId) {
        String sql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    private void saveTagsForPost(Long postId, Set<Tag> tags) {
        for (Tag tag : tags) {
            Long tagId = findOrCreateTag(tag.getName());
            String sql = "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?) " +
                    "ON CONFLICT (post_id, tag_id) DO NOTHING";
            jdbcTemplate.update(sql, postId, tagId);
        }
    }

    private Long findOrCreateTag(String tagName) {
        String findSql = "SELECT id FROM tags WHERE name = ?";
        List<Long> tagIds = jdbcTemplate.query(findSql, (rs, rowNum) -> rs.getLong("id"), tagName);

        if (!tagIds.isEmpty()) {
            return tagIds.get(0);
        }

        String insertSql = "INSERT INTO tags (name) VALUES (?) RETURNING id";
        return jdbcTemplate.queryForObject(insertSql, Long.class, tagName);
    }
}
