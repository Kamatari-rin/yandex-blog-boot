package org.example.junit.service;

import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.dto.UserDTO;
import org.example.enums.LikeTargetType;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.example.mapper.PostMapper;
import org.example.service.TagService;
import org.example.service.UserServiceClient;
import org.example.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private PostServiceImpl postService;

    private Post samplePost;
    private PostDTO samplePostDTO;

    @BeforeEach
    void setUp() {
        samplePost = Post.builder()
                .id(1L)
                .title("Sample Title")
                .content("Sample Content")
                .userId(1L)
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedAt(Instant.now().minusSeconds(3600))
                .build();
        samplePostDTO = PostDTO.builder()
                .id(1L)
                .title("Sample Title")
                .content("Sample Content")
                .userId(1L)
                .build();
    }

    @Test
    void testGetPostById() {
        when(postRepository.findPostById(1L)).thenReturn(Optional.of(samplePost));
        Set<Tag> tags = new HashSet<>(Arrays.asList(
                Tag.builder().id(1L).name("tag1").build(),
                Tag.builder().id(2L).name("tag2").build()
        ));
        when(tagService.getTagsForPost(1L)).thenReturn(tags);
        when(postMapper.toPostDTO(samplePost)).thenReturn(samplePostDTO);
        when(userServiceClient.fetchUserById(1L))
                .thenReturn(new UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));

        PostDTO result = postService.getPostById(1L);
        assertNotNull(result);
        assertEquals(samplePostDTO.getId(), result.getId());
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    void testGetAllPosts() {
        List<Post> posts = List.of(samplePost);
        when(postRepository.findAllPosts(anyInt(), anyInt())).thenReturn(posts);
        Set<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        Map<Long, Set<Tag>> tagsByPost = new HashMap<>();
        tagsByPost.put(1L, new HashSet<>(Collections.singletonList(Tag.builder().id(1L).name("tag1").build())));
        when(tagService.getTagsForPosts(postIds)).thenReturn(tagsByPost);
        when(postMapper.toPostDTO(samplePost)).thenReturn(samplePostDTO);
        when(userServiceClient.fetchUserById(anyLong()))
                .thenReturn(new org.example.dto.UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(anyLong(), eq(LikeTargetType.POST))).thenReturn(5);
        when(postRepository.countCommentsByPostId(anyLong())).thenReturn(10);

        List<PostDTO> result = postService.getAllPosts(100, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(samplePostDTO.getId(), result.get(0).getId());
    }

    @Test
    void testCreatePost() {
        Set<String> tagNames = new HashSet<>(Arrays.asList("tag1", "tag2"));
        PostCreateDTO createDTO = PostCreateDTO.builder()
                .userId(1L)
                .title("New Post")
                .content("Post content")
                .imageUrl("imageUrl")
                .tags(tagNames)
                .build();

        Post postToSave = Post.builder()
                .title("New Post")
                .content("Post content")
                .userId(1L)
                .build();

        Post savedPost = Post.builder()
                .id(2L)
                .title("New Post")
                .content("Post content")
                .userId(1L)
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedAt(Instant.now())
                .build();

        PostDTO expectedDTO = PostDTO.builder()
                .id(2L)
                .title("New Post")
                .content("Post content")
                .userId(1L)
                .build();
        expectedDTO.setAuthorName("Author");
        expectedDTO.setLikesCount(5);
        expectedDTO.setCommentsCount(10);
        expectedDTO.setTags(tagNames);

        when(postMapper.toEntity(createDTO)).thenReturn(postToSave);
        when(postRepository.save(postToSave)).thenReturn(savedPost);
        Set<Tag> persistedTags = tagNames.stream()
                .map(tagName -> Tag.builder().id(new Random().nextLong()).name(tagName).build())
                .collect(Collectors.toSet());
        when(tagService.findOrCreateTag(anyString())).thenAnswer(invocation -> {
            String tName = invocation.getArgument(0);
            return persistedTags.stream().filter(t -> t.getName().equals(tName)).findFirst().orElse(null);
        });
        doNothing().when(tagService).deleteTagsForPost(savedPost.getId());
        doNothing().when(tagService).saveTagsForPost(savedPost.getId(), persistedTags);

        when(postMapper.toPostDTO(savedPost)).thenReturn(expectedDTO);
        when(userServiceClient.fetchUserById(1L))
                .thenReturn(new org.example.dto.UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(2L, LikeTargetType.POST)).thenReturn(5);
        when(postRepository.countCommentsByPostId(2L)).thenReturn(10);

        PostDTO result = postService.createPost(createDTO);
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getContent(), result.getContent());
        assertEquals(expectedDTO.getTags(), result.getTags());
    }

    @Test
    void testUpdatePost() {
        Set<String> tagNames = new HashSet<>(Arrays.asList("tag1", "tag3"));
        PostUpdateDTO updateDTO = PostUpdateDTO.builder()
                .postId(1L)
                .userId(1L)
                .title("Updated Title")
                .content("Updated Content")
                .imageUrl("image.png")
                .tags(tagNames)
                .build();

        Post existingPost = Post.builder()
                .id(1L)
                .title("Old Title")
                .content("Old Content")
                .userId(1L)
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedAt(Instant.now().minusSeconds(3600))
                .build();

        PostDTO expectedUpdatedDTO = PostDTO.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .userId(1L)
                .build();
        expectedUpdatedDTO.setAuthorName("Author");
        expectedUpdatedDTO.setLikesCount(5);
        expectedUpdatedDTO.setCommentsCount(10);
        expectedUpdatedDTO.setTags(tagNames);

        when(postRepository.findPostById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);

        Set<Tag> persistedTags = tagNames.stream()
                .map(tagName -> Tag.builder().id(new Random().nextLong()).name(tagName).build())
                .collect(Collectors.toSet());
        when(tagService.findOrCreateTag(anyString())).thenAnswer(invocation -> {
            String tName = invocation.getArgument(0);
            return persistedTags.stream().filter(t -> t.getName().equals(tName)).findFirst().orElse(null);
        });
        doNothing().when(tagService).deleteTagsForPost(1L);
        doNothing().when(tagService).saveTagsForPost(1L, persistedTags);

        when(postMapper.toPostDTO(existingPost)).thenReturn(expectedUpdatedDTO);
        when(userServiceClient.fetchUserById(1L))
                .thenReturn(new org.example.dto.UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(1L, LikeTargetType.POST)).thenReturn(5);
        when(postRepository.countCommentsByPostId(1L)).thenReturn(10);

        PostDTO result = postService.updatePost(updateDTO);
        assertNotNull(result);
        assertEquals(expectedUpdatedDTO.getTitle(), result.getTitle());
        assertEquals(expectedUpdatedDTO.getContent(), result.getContent());
        assertEquals(expectedUpdatedDTO.getTags(), result.getTags());
    }

    @Test
    void testDeletePost() {
        Long postId = 1L;
        doNothing().when(postRepository).deletePostById(postId);
        postService.deletePost(postId);
        verify(postRepository, times(1)).deletePostById(postId);
    }

    @Test
    void testGetPostsByTag() {
        String tag = "tag1";

        Post post1 = Post.builder()
                .id(1L)
                .title("Post 1")
                .content("Content 1")
                .userId(1L)
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedAt(Instant.now())
                .build();
        Post post2 = Post.builder()
                .id(2L)
                .title("Post 2")
                .content("Content 2")
                .userId(2L)
                .createdAt(Instant.now().minusSeconds(1800))
                .updatedAt(Instant.now())
                .build();

        List<Post> postsByTag = List.of(post1, post2);
        when(postRepository.findPostsByTag(tag)).thenReturn(postsByTag);

        Map<Long, Set<Tag>> tagsByPost = new HashMap<>();
        tagsByPost.put(1L, new HashSet<>(Collections.singletonList(Tag.builder().id(1L).name("tag1").build())));
        tagsByPost.put(2L, new HashSet<>(Collections.singletonList(Tag.builder().id(2L).name("tag2").build())));
        when(tagService.getTagsForPosts(anySet())).thenReturn(tagsByPost);

        when(userServiceClient.fetchUserById(anyLong()))
                .thenReturn(new org.example.dto.UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(anyLong(), eq(LikeTargetType.POST))).thenReturn(5);
        when(postRepository.countCommentsByPostId(anyLong())).thenReturn(10);
        when(postMapper.toPostDTO(any(Post.class))).thenAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            return PostDTO.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .content(p.getContent())
                    .userId(p.getUserId())
                    .build();
        });

        List<PostDTO> result = postService.getPostsByTag(tag);
        assertEquals(2, result.size(), "Количество постов не совпадает");
    }
}
