package org.example.junit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.junit.config.LikeControllerTestConfig;
import org.example.controller.LikeController;
import org.example.dto.CreateLikeDTO;
import org.example.dto.LikeDTO;
import org.example.enums.LikeTargetType;
import org.example.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LikeControllerTestConfig.class)
public class LikeControllerTest {

    @Autowired
    private LikeController likeController;

    private MockMvc mockMvc;

    @Autowired
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(likeController)
                .build();
    }

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
                    assertTrue(responseBody.contains(String.valueOf(expectedCount)));
                });
    }

    @Test
    void testSaveOrUpdateLike() throws Exception {
        CreateLikeDTO createLikeDTO = new CreateLikeDTO(1L, 1L, LikeTargetType.POST, true);
        LikeDTO expectedLikeDTO = new LikeDTO(1L, 1L, 1L, LikeTargetType.POST, true);

        when(likeService.saveOrUpdateLike(any())).thenReturn(expectedLikeDTO);

        mockMvc.perform(post("/api/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createLikeDTO)))
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
