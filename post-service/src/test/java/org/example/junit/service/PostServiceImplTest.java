//package org.example.junit.service;
//
//import org.example.junit.config.PostServiceTestConfig;
//import org.example.dto.PostCreateDTO;
//import org.example.dto.PostDTO;
//import org.example.dto.PostUpdateDTO;
//import org.example.dto.UserDTO;
//import org.example.enums.LikeTargetType;
//import org.example.model.Post;
//import org.example.repository.PostRepository;
//import org.example.mapper.PostMapper;
//import org.example.service.PostService;
//import org.example.service.UserServiceClient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = PostServiceTestConfig.class)
//public class PostServiceImplTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private UserServiceClient userServiceClient;
//
//    @Autowired
//    private PostMapper postMapper;
//
//    @BeforeEach
//    void setUp() {
//        reset(postRepository);
//    }
//
//    private PostDTO createPostDTO(Long id, String title, String content) {
//        return PostDTO.builder()
//                .id(id)
//                .title(title)
//                .content(content)
//                .userId(1L)
//                .build();
//    }
//
//    private PostCreateDTO createPostCreateDTO(Long userId, String title, String content) {
//        return PostCreateDTO.builder()
//                .userId(userId)
//                .title(title)
//                .content(content)
//                .imageUrl("imageUrl")
//                .tags(null)
//                .build();
//    }
//
//    private void assertPostDTOEquals(PostDTO expected, PostDTO actual) {
//        assertNotNull(actual, "Пост не должен быть null");
//        assertEquals(expected.getId(), actual.getId(), "ID поста не совпадает");
//        assertEquals(expected.getTitle(), actual.getTitle(), "Название поста не совпадает");
//        assertEquals(expected.getContent(), actual.getContent(), "Содержание поста не совпадает");
//    }
//
//    @Test
//    void testGetPostById() {
//        Long postId = 1L;
//        PostDTO expectedPost = createPostDTO(postId, "Test Title", "Test Content");
//
//        when(postRepository.findPostWithTagsById(postId)).thenReturn(Optional.of(new Post()));
//        when(postMapper.toPostDTO(any(Post.class), eq(userServiceClient), eq(postRepository))).thenReturn(expectedPost);
//
//        PostDTO result = postService.getPostById(postId);
//
//        assertPostDTOEquals(expectedPost, result);
//    }
//
//    @Test
//    void testCreatePost() {
//        PostCreateDTO postCreateDTO = createPostCreateDTO(1L, "New Post", "Post content");
//        PostDTO expectedPostDTO = createPostDTO(2L, "New Post", "Post content");
//
//        Post postToSave = new Post();
//        when(postMapper.toEntity(postCreateDTO)).thenReturn(postToSave);
//        when(postRepository.save(postToSave)).thenReturn(postToSave);
//        when(postMapper.toPostDTO(postToSave, userServiceClient, postRepository)).thenReturn(expectedPostDTO);
//
//        PostDTO result = postService.createPost(postCreateDTO);
//
//        assertPostDTOEquals(expectedPostDTO, result);
//    }
//
//    @Test
//    void testUpdatePost() {
//        PostUpdateDTO updatePostDTO = new PostUpdateDTO(1L, 1L, "Updated Title", "Updated Content", "imageUrl", null);
//        PostDTO expectedUpdatedPostDTO = createPostDTO(1L, "Updated Title", "Updated Content");
//
//        Post existingPost = new Post();
//        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
//        when(postMapper.toEntity(updatePostDTO)).thenReturn(new Post());
//        when(postRepository.update(any(Post.class))).thenReturn(new Post());
//        when(postMapper.toPostDTO(any(Post.class), eq(userServiceClient), eq(postRepository))).thenReturn(expectedUpdatedPostDTO);
//
//        PostDTO result = postService.updatePost(updatePostDTO);
//
//        assertPostDTOEquals(expectedUpdatedPostDTO, result);
//    }
//
//    @Test
//    void testDeletePost() {
//        Long postId = 1L;
//
//        doNothing().when(postRepository).delete(postId);
//
//        postService.deletePost(postId);
//
//        verify(postRepository, times(1)).delete(postId);
//    }
//
//    @Test
//    void testGetPostsByTag() {
//        String tag = "tag1";
//
//        List<PostDTO> expectedPosts = List.of(
//                createPostDTO(1L, "Post 1", "Content"),
//                createPostDTO(2L, "Post 2", "Content")
//        );
//
//        Post post1 = Post.builder()
//                .id(1L)
//                .title("Post 1")
//                .content("Content")
//                .userId(1L)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        Post post2 = Post.builder()
//                .id(2L)
//                .title("Post 2")
//                .content("Content")
//                .userId(2L)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        when(postRepository.findPostsByTag(tag)).thenReturn(List.of(post1, post2));
//
//        doReturn(5).when(userServiceClient).fetchLikesCountByTarget(any(), eq(LikeTargetType.POST));
//        doReturn(10).when(postRepository).countCommentsByPostId(any());
//        doReturn(new UserDTO(1L, "Author", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()))
//                .when(userServiceClient).fetchUserById(any());
//
//        when(postMapper.toPostDTO(any(Post.class), eq(userServiceClient), eq(postRepository)))
//                .thenAnswer(invocation -> {
//                    Post post = invocation.getArgument(0);
//                    Long id = post.getId();
//                    return PostDTO.builder()
//                            .id(id)
//                            .title("Post " + id)
//                            .content("Content")
//                            .userId(post.getUserId())
//                            .build();
//                });
//
//        List<PostDTO> result = postService.getPostsByTag(tag);
//
//        assertEquals(expectedPosts.size(), result.size(), "Размер списков постов не совпадает");
//
//        for (int i = 0; i < expectedPosts.size(); i++) {
//            assertPostDTOEquals(expectedPosts.get(i), result.get(i));
//        }
//    }
//}
