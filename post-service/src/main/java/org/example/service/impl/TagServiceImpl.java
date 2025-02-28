package org.example.service.impl;

import org.example.model.PostTag;
import org.example.model.Tag;
import org.example.repository.PostTagRepository;
import org.example.repository.TagRepository;
import org.example.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public TagServiceImpl(TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
    }

    @Override
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
    }

    @Override
    public Set<Tag> getTagsForPost(Long postId) {
        return postTagRepository.findTagsByPostId(postId);
    }

    @Override
    @Transactional
    public void saveTagsForPost(Long postId, Set<Tag> tags) {
        for (Tag tag : tags) {
            PostTag postTag = PostTag.builder()
                    .postId(postId)
                    .tagId(tag.getId())
                    .build();
            postTagRepository.save(postTag);
        }
    }

    @Override
    public Map<Long, Set<Tag>> getTagsForPosts(Set<Long> postIds) {
        return postTagRepository.findTagsByPostIds(postIds);
    }

    @Override
    @Transactional
    public void deleteTagsForPost(Long postId) {
        postTagRepository.deleteByPostId(postId);
    }
}
