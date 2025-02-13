package org.example.integration.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.PostController;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.enums.LikeTargetType;
import org.example.integration.config.PostIntegrationTestConfig;
import org.example.service.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostIntegrationTestConfig.class)
@ActiveProfiles("test")
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostController postController;

    @Autowired
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();
    }

    @Test
    void testCreatePost() throws Exception {
        PostCreateDTO postCreateDTO = PostCreateDTO.builder()
                .userId(1L)
                .title("Test Post")
                .imageUrl("http://example.com/image.jpg")
                .content("This is a test post content.")
                .tags(Set.of("tag1", "tag2"))
                .build();

        PostDTO expectedPostDTO = PostDTO.builder()
                .id(2L)
                .userId(1L)
                .title("Test Post")
                .content("This is a test post content.")
                .authorName("Author Name")
                .tags(Set.of("tag1", "tag2"))
                .build();

        when(userServiceClient.fetchLikesCountByTarget(anyLong(), eq(LikeTargetType.POST))).thenReturn(10);
        when(userServiceClient.fetchUserById(1L)).thenReturn(new UserDTO(1L, "Author Name", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"id\":" + expectedPostDTO.getId()), "ID поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"userId\":" + expectedPostDTO.getUserId()), "userId не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"title\":\"" + expectedPostDTO.getTitle() + "\""), "Заголовок поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"content\":\"" + expectedPostDTO.getContent() + "\""), "Контент поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"authorName\":\"" + expectedPostDTO.getAuthorName() + "\""), "Имя автора не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"tags\":[\"tag1\",\"tag2\"]"), "Теги поста не совпадают с ожидаемыми");
                });
    }
}
