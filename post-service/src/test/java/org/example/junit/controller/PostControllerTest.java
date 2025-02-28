//package org.example.junit.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.junit.config.PostControllerTestConfig;
//import org.example.controller.PostController;
//import org.example.dto.PostCreateDTO;
//import org.example.dto.PostDTO;
//import org.example.dto.PostUpdateDTO;
//import org.example.service.PostService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = PostControllerTestConfig.class)
//public class PostControllerTest {
//
//    @Autowired
//    private PostController postController;
//
//    @Autowired
//    private PostService postService;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        this.objectMapper = new ObjectMapper();
//        this.mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
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
//    private void assertPostDTOEquals(PostDTO expected, PostDTO actual) {
//        assertNotNull(actual, "Пост не должен быть null");
//        assertEquals(expected.getId(), actual.getId(), "ID поста не совпадает");
//        assertEquals(expected.getTitle(), actual.getTitle(), "Название поста не совпадает");
//        assertEquals(expected.getContent(), actual.getContent(), "Содержание поста не совпадает");
//    }
//
//    @Test
//    void testGetPostById() throws Exception {
//        PostDTO expectedPost = createPostDTO(1L, "Test Title", "Test Content");
//
//        when(postService.getPostById(1L)).thenReturn(expectedPost);
//
//        mockMvc.perform(get("/api/posts/1"))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    PostDTO actualPost = objectMapper.readValue(result.getResponse().getContentAsString(), PostDTO.class);
//                    assertPostDTOEquals(expectedPost, actualPost);
//                });
//    }
//
//    @Test
//    void testCreatePost() throws Exception {
//        PostCreateDTO createPostDTO = new PostCreateDTO(1L, "New Post", "Image URL", "Some content", null);
//        PostDTO expectedPost = createPostDTO(2L, "New Post", "Some content");
//
//        when(postService.createPost(any(PostCreateDTO.class))).thenReturn(expectedPost);
//
//        mockMvc.perform(post("/api/posts")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(createPostDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(result -> {
//                    PostDTO actualPost = objectMapper.readValue(result.getResponse().getContentAsString(), PostDTO.class);
//                    assertPostDTOEquals(expectedPost, actualPost);
//                });
//    }
//
//    @Test
//    void testUpdatePost() throws Exception {
//        PostUpdateDTO updatePostDTO = new PostUpdateDTO(1L, 1L, "Updated Title", "Updated Content", "image.png", null);
//        PostDTO updatedPost = createPostDTO(1L, "Updated Title", "Updated Content");
//
//        when(postService.updatePost(any(PostUpdateDTO.class))).thenReturn(updatedPost);
//
//        mockMvc.perform(put("/api/posts/1")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(updatePostDTO)))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    PostDTO actualPost = objectMapper.readValue(result.getResponse().getContentAsString(), PostDTO.class);
//                    assertPostDTOEquals(updatedPost, actualPost);
//                });
//    }
//
//    @Test
//    void testDeletePost() throws Exception {
//        Long postId = 1L;
//
//        mockMvc.perform(delete("/api/posts/{id}", postId))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testGetAllPosts() throws Exception {
//        // Подготовка данных
//        List<PostDTO> expectedPosts = List.of(
//                createPostDTO(1L, "Post 1", "Content 1"),
//                createPostDTO(2L, "Post 2", "Content 2")
//        );
//
//        when(postService.getAllPosts(100, 0)).thenReturn(expectedPosts);
//
//        mockMvc.perform(get("/api/posts")
//                        .param("limit", "100")
//                        .param("offset", "0"))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    List<PostDTO> actualPosts = objectMapper.readValue(result.getResponse().getContentAsString(),
//                            new TypeReference<List<PostDTO>>() {});
//                    assertEquals(expectedPosts.size(), actualPosts.size(), "Количество постов не совпадает");
//                    for (int i = 0; i < expectedPosts.size(); i++) {
//                        assertPostDTOEquals(expectedPosts.get(i), actualPosts.get(i));
//                    }
//                });
//    }
//}
