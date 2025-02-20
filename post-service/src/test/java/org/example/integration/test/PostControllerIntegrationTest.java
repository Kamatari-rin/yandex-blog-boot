package org.example.integration.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.PostController;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostDTO;
import org.example.dto.PostUpdateDTO;
import org.example.dto.UserDTO;
import org.example.enums.LikeTargetType;
import org.example.integration.config.PostIntegrationTestConfig;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.example.service.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostIntegrationTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostController postController;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        Tag tagOne = Tag.builder()
                .name("tag1")
                .build();
        Tag tagTwo = Tag.builder()
                .name("tag2")
                .build();

        Post postTest = Post.builder()
                .userId(1L)
                .title("Test Post")
                .imageUrl("http://example.com/image.jpg")
                .content("This is a test post content.")
                .tags(Set.of(tagOne, tagTwo))
                .build();
        postRepository.deleteAllPosts();
        Post saved = postRepository.save(postTest);

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

    @Test
    void testGetPostById() throws Exception {
        Long postId = 1L;
        PostDTO expectedPostDTO = PostDTO.builder()
                .id(postId)
                .userId(1L)
                .title("Test Post")
                .content("This is a test post content.")
                .authorName("Author Name")
                .tags(Set.of("tag1", "tag2"))
                .likesCount(10)
                .commentsCount(5)
                .build();

        when(userServiceClient.fetchUserById(anyLong()))
                .thenReturn(new UserDTO(1L, "Author Name", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(anyLong(), eq(LikeTargetType.POST)))
                .thenReturn(10);

        MvcResult result = mockMvc.perform(get("/api/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);

        assertEquals(expectedPostDTO.getId(), json.get("id").asLong(), "ID поста не совпадает с ожидаемым");
        assertEquals(expectedPostDTO.getUserId(), json.get("userId").asLong(), "userId не совпадает с ожидаемым");
        assertEquals(expectedPostDTO.getTitle(), json.get("title").asText(), "Заголовок поста не совпадает с ожидаемым");
        assertEquals(expectedPostDTO.getContent(), json.get("content").asText(), "Контент поста не совпадает с ожидаемым");
        assertEquals(expectedPostDTO.getAuthorName(), json.get("authorName").asText(), "Имя автора не совпадает с ожидаемым");
        assertEquals(10, json.get("likesCount").asInt(), "Количество лайков не совпадает с ожидаемым");
//        assertEquals(5, json.get("commentsCount").asInt(), "Количество комментариев не совпадает с ожидаемым");

        JsonNode tagsNode = json.get("tags");
        assertNotNull(tagsNode, "Поле tags отсутствует");
        assertTrue(tagsNode.isArray(), "Поле tags не является массивом");

        Set<String> returnedTags = new HashSet<>();
        for (JsonNode tagNode : tagsNode) {
            returnedTags.add(tagNode.asText());
        }
        assertEquals(expectedPostDTO.getTags(), returnedTags, "Теги поста не совпадают с ожидаемыми");
    }

    @Test
    void testUpdatePost() throws Exception {
        Long postId = 1L;

        PostUpdateDTO updateDTO = PostUpdateDTO.builder()
                .title("Updated Test Post")
                .content("This is the updated content.")
                .imageUrl("http://example.com/updated_image.jpg")
                .tags(Set.of(new Tag(null,"tag1"), new Tag(null, "tag3")))
                .build();
        updateDTO.setPostId(postId);

        PostDTO expectedPostDTO = PostDTO.builder()
                .id(postId)
                .userId(1L)
                .title("Updated Test Post")
                .content("This is the updated content.")
                .imageUrl("http://example.com/updated_image.jpg")
                .authorName("Author Name")
                .tags(Set.of("tag1", "tag3"))
                .likesCount(10)
                .commentsCount(5)
                .build();

        when(userServiceClient.fetchUserById(anyLong()))
                .thenReturn(new UserDTO(1L, "Author Name", "author@example.com", Instant.now(), Instant.now(), new HashSet<>(), new HashSet<>()));
        when(userServiceClient.fetchLikesCountByTarget(anyLong(), eq(LikeTargetType.POST)))
                .thenReturn(10);

        mockMvc.perform(put("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"id\":" + expectedPostDTO.getId()), "ID поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"title\":\"" + expectedPostDTO.getTitle() + "\""), "Заголовок поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"content\":\"" + expectedPostDTO.getContent() + "\""), "Контент поста не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"imageUrl\":\"" + expectedPostDTO.getImageUrl() + "\""), "URL изображения не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"authorName\":\"" + expectedPostDTO.getAuthorName() + "\""), "Имя автора не совпадает с ожидаемым");
                    assertTrue(responseBody.contains("\"likesCount\":10"), "Количество лайков не совпадает с ожидаемым");

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(responseBody);
                    JsonNode tagsNode = json.get("tags");
                    assertNotNull(tagsNode, "Поле tags отсутствует в ответе");
                    assertTrue(tagsNode.isArray(), "Поле tags не является массивом");

                    Set<String> returnedTags = new HashSet<>();
                    for (JsonNode tagNode : tagsNode) {
                        returnedTags.add(tagNode.asText());
                    }
                    assertEquals(expectedPostDTO.getTags(), returnedTags, "Теги поста не совпадают с ожидаемыми");
                });
    }

    @Test
    void testGetAllPostsWithoutTag() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                        .param("limit", "100")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        assertTrue(json.isArray(), "Ответ не является массивом");
        assertTrue(json.size() > 0, "Массив постов пуст, а ожидались данные");
    }

    @Test
    void testGetAllPostsWithExistingTag() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                        .param("tag", "tag1")
                        .param("limit", "100")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        assertTrue(json.isArray(), "Ответ не является массивом");

        for (JsonNode postNode : json) {
            JsonNode tagsNode = postNode.get("tags");
            assertNotNull(tagsNode, "У поста отсутствует поле tags");
            assertTrue(tagsNode.isArray(), "Поле tags не является массивом");

            Set<String> returnedTags = new HashSet<>();
            for (JsonNode tagNode : tagsNode) {
                returnedTags.add(tagNode.asText());
            }
            assertTrue(returnedTags.contains("tag1"), "Пост не содержит тег 'tag1'");
        }
    }

    @Test
    void testGetAllPostsWithNonExistentTag() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                        .param("tag", "nonexistent")
                        .param("limit", "100")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);

        assertTrue(json.isArray(), "Ответ не является массивом");
        assertEquals(0, json.size(), "Ожидается пустой массив постов при несуществующем теге");
    }

    @Test
    void testDeletePost() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete("/api/posts/{postId}", postId))
                .andExpect(status().isNoContent());

        Optional<Post> deletedPost = postRepository.findById(postId);
        assertTrue(deletedPost.isEmpty(), "Пост не был удален из репозитория");
    }

}
