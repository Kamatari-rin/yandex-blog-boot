package org.example.junit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.PostController;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private PostDTO createPostDTO(Long id, String title, String content) {
        return PostDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .userId(1L)
                .build();
    }

    private void assertPostDTOEquals(PostDTO expected, PostDTO actual) {
        assertNotNull(actual, "Пост не должен быть null");
        assertEquals(expected.getId(), actual.getId(), "ID поста не совпадает");
        assertEquals(expected.getTitle(), actual.getTitle(), "Название поста не совпадает");
        assertEquals(expected.getContent(), actual.getContent(), "Содержание поста не совпадает");
    }

    @Test
    void testGetPostById() throws Exception {
        PostDTO expectedPost = createPostDTO(1L, "Test Title", "Test Content");
        when(postService.getPostById(1L)).thenReturn(expectedPost);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    PostDTO actualPost = objectMapper.readValue(
                            result.getResponse().getContentAsString(), PostDTO.class);
                    assertPostDTOEquals(expectedPost, actualPost);
                });
    }

    @Test
    void testCreatePost() throws Exception {
        PostCreateDTO createPostDTO = new PostCreateDTO(1L, "New Post", "Image URL", "Some content", null);
        PostDTO expectedPost = createPostDTO(2L, "New Post", "Some content");

        when(postService.createPost(any(PostCreateDTO.class))).thenReturn(expectedPost);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    PostDTO actualPost = objectMapper.readValue(
                            result.getResponse().getContentAsString(), PostDTO.class);
                    assertPostDTOEquals(expectedPost, actualPost);
                });
    }

    @Test
    void testUpdatePost() throws Exception {
        PostUpdateDTO updatePostDTO = new PostUpdateDTO(1L, 1L, "Updated Title", "Updated Content", "image.png", null);
        PostDTO updatedPost = createPostDTO(1L, "Updated Title", "Updated Content");

        when(postService.updatePost(any(PostUpdateDTO.class))).thenReturn(updatedPost);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePostDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    PostDTO actualPost = objectMapper.readValue(
                            result.getResponse().getContentAsString(), PostDTO.class);
                    assertPostDTOEquals(updatedPost, actualPost);
                });
    }

    @Test
    void testDeletePost() throws Exception {
        Long postId = 1L;
        mockMvc.perform(delete("/api/posts/{id}", postId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllPosts() throws Exception {
        List<PostDTO> expectedPosts = List.of(
                createPostDTO(1L, "Post 1", "Content 1"),
                createPostDTO(2L, "Post 2", "Content 2")
        );

        when(postService.getAllPosts(100, 0)).thenReturn(expectedPosts);

        mockMvc.perform(get("/api/posts")
                        .param("limit", "100")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<PostDTO> actualPosts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<PostDTO>>() {});
                    assertEquals(expectedPosts.size(), actualPosts.size(), "Количество постов не совпадает");
                    for (int i = 0; i < expectedPosts.size(); i++) {
                        assertPostDTOEquals(expectedPosts.get(i), actualPosts.get(i));
                    }
                });
    }
}
