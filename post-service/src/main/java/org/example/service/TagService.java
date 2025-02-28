package org.example.service;

import org.example.model.Tag;

import java.util.Map;
import java.util.Set;

public interface TagService {
    Tag findOrCreateTag(String tagName);
    Set<Tag> getTagsForPost(Long postId);

    void saveTagsForPost(Long postId, Set<Tag> tags);

    void deleteTagsForPost(Long postId);

    Map<Long, Set<Tag>> getTagsForPosts(Set<Long> postIds);
}
