package org.example.repository.impl;

import org.example.mapper.TagRowMapper;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.util.stream.Collectors;


public class PostRepositoryImpl extends AbstractRepositoryImpl<Post> implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> postRowMapper;
    private final RowMapper<Post> postRowMapperWithTags;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Post> postRowMapper, RowMapper<Post> postRowMapperWithTags) {
        super(jdbcTemplate, postRowMapper);
        this.jdbcTemplate = jdbcTemplate;
        this.postRowMapper = postRowMapper;
        this.postRowMapperWithTags = postRowMapperWithTags;
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
            savedPost.setTags(saveAndGetTagsForPost(savedPost.getId(), post.getTags()));
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
            updatedPost.setTags(saveAndGetTagsForPost(updatedPost.getId(), post.getTags()));
        }

        return updatedPost;
    }

    @Override
    public int countCommentsByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM post_service.comments WHERE post_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }

    @Override
    public List<Post> findAllPostsWithTags(int limit, int offset) {
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
        FROM post_service.posts p
        LEFT JOIN post_service.post_tags pt ON p.id = pt.post_id
        LEFT JOIN post_service.tags t ON pt.tag_id = t.id
        GROUP BY p.id
        ORDER BY p.created_at DESC
        LIMIT ? OFFSET ?
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
        }, limit, offset);
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
        FROM post_service.posts p
        JOIN post_service.post_tags pt ON p.id = pt.post_id
        JOIN post_service.tags t ON pt.tag_id = t.id
        WHERE t.name ILIKE ?
        GROUP BY p.id
        ORDER BY p.created_at DESC
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
        }, "%" + tagName + "%");
    }

    @Override
    public Optional<Post> findPostWithTagsById(Long id) {
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
        FROM post_service.posts p
        LEFT JOIN post_service.post_tags pt ON p.id = pt.post_id
        LEFT JOIN post_service.tags t ON pt.tag_id = t.id
        WHERE p.id = ?
        GROUP BY p.id
    """;
        try {
            Post post = jdbcTemplate.queryForObject(sql, postRowMapperWithTags, id);
            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private void deleteTagsForPost(Long postId) {
        String sql = "DELETE FROM post_service.post_tags WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    private Set<Tag> saveAndGetTagsForPost(Long postId, Set<Tag> tags) {
        for (Tag tag : tags) {
            Long tagId = findOrCreateTag(tag.getName());
            String sql = "INSERT INTO post_service.post_tags (post_id, tag_id) VALUES (?, ?) " +
                    "ON CONFLICT (post_id, tag_id) DO NOTHING";
            jdbcTemplate.update(sql, postId, tagId);
        }
        return getTagsByPostId(postId);
    }

    private Set<Tag> getTagsByPostId(Long postId) {
        String sql = "SELECT t.id, t.name FROM post_service.tags t " +
                "JOIN post_service.post_tags pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, new TagRowMapper(), postId));
    }

    private Long findOrCreateTag(String tagName) {
        String findSql = "SELECT id FROM post_service.tags WHERE name = ?";
        List<Long> tagIds = jdbcTemplate.query(findSql, (rs, rowNum) -> rs.getLong("id"), tagName);

        if (!tagIds.isEmpty()) {
            return tagIds.get(0);
        }

        String insertSql = "INSERT INTO post_service.tags (name) VALUES (?) RETURNING id";
        return jdbcTemplate.queryForObject(insertSql, Long.class, tagName);
    }

    @Override
    public void deleteAllPosts() {
        jdbcTemplate.update("TRUNCATE TABLE post_service.post_tags RESTART IDENTITY CASCADE");
        jdbcTemplate.update("TRUNCATE TABLE post_service.posts RESTART IDENTITY CASCADE");
        jdbcTemplate.update("TRUNCATE TABLE post_service.tags RESTART IDENTITY CASCADE");
    }
}
