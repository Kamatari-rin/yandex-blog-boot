package org.example.junit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.LikeController;
import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.service.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Objects;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@ActiveProfiles("test")
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LikeService likeService;

    @Test
    void testCountLikes() throws Exception {
        Long targetId = 1L;
        LikeTargetType targetType = LikeTargetType.POST;
        int expectedCount = 5;

        when(likeService.countLikesByIdAndTarget(targetId, targetType)).thenReturn(expectedCount);

        mockMvc.perform(get("/api/likes/count")
                        .param("targetId", targetId.toString())
                        .param("targetType", targetType.name()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(Objects.requireNonNull(responseBody).contains(String.valueOf(expectedCount)));
                });
    }

    @Test
    void testSaveOrUpdateLike() throws Exception {
        CreateLikeDTO createLikeDTO = new CreateLikeDTO(1L, 1L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = new LikeDTO(1L, 1L, 1L, LikeTargetType.POST, true);

        when(likeService.saveOrUpdateLike(any())).thenReturn(expectedLikeDTO);

        mockMvc.perform(post("/api/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createLikeDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"id\":" + expectedLikeDTO.getId()));
                    assertTrue(responseBody.contains("\"userId\":" + expectedLikeDTO.getUserId()));
                    assertTrue(responseBody.contains("\"targetId\":" + expectedLikeDTO.getTargetId()));
                    assertTrue(responseBody.contains("\"targetType\":\"" + expectedLikeDTO.getTargetType().name() + "\""));
                    assertTrue(responseBody.contains("\"liked\":" + expectedLikeDTO.isLiked()));
                });
    }
}
