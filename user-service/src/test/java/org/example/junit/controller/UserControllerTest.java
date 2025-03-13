package org.example.junit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.UserController;
import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    private UserDTO createUserDTO(Long id, String username, String email) {
        return UserDTO.builder()
                .id(id)
                .username(username)
                .email(email)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .likedPosts(new HashSet<>())
                .dislikedPosts(new HashSet<>())
                .build();
    }

    private void assertUserDTOEquals(UserDTO expected, UserDTO actual) {
        assertNotNull(actual, "Пользователь не должен быть null");
        assertEquals(expected.getId(), actual.getId(), "ID пользователя не совпадает");
        assertEquals(expected.getUsername(), actual.getUsername(), "Имя пользователя не совпадает");
        assertEquals(expected.getEmail(), actual.getEmail(), "Email пользователя не совпадает");
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt(), "Время создания пользователя не совпадает");
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt(), "Время обновления пользователя не совпадает");
        assertEquals(expected.getLikedPosts(), actual.getLikedPosts(), "Набор понравившихся постов не совпадает");
        assertEquals(expected.getDislikedPosts(), actual.getDislikedPosts(), "Набор не понравившихся постов не совпадает");
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDTO newUserDTO = CreateUserDTO.builder()
                .username("newUser")
                .email("newuser@example.com")
                .password("password123")
                .build();

        UserDTO newUser = createUserDTO(2L, "newUser", "newuser@example.com");

        when(userService.createUser(any())).thenReturn(newUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    UserDTO actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
                    assertUserDTOEquals(newUser, actualUser);
                });
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO expectedUser = createUserDTO(1L, "existingUser", "existing@example.com");

        when(userService.getUserById(1L)).thenReturn(expectedUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    UserDTO actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
                    assertUserDTOEquals(expectedUser, actualUser);
                });
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> expectedUsers = List.of(
                createUserDTO(1L, "user1", "user1@example.com"),
                createUserDTO(2L, "user2", "user2@example.com")
        );

        when(userService.getAllUsers(10, 0)).thenReturn(expectedUsers);

        mockMvc.perform(get("/api/users")
                        .param("limit", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<UserDTO> actualUsers = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<UserDTO>>() {}
                    );
                    assertEquals(expectedUsers.size(), actualUsers.size(), "Размер списков не совпадает");
                    IntStream.range(0, expectedUsers.size())
                            .forEach(i -> assertUserDTOEquals(expectedUsers.get(i), actualUsers.get(i)));
                });
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .username("updatedUser")
                .email("updateduser@example.com")
                .build();

        UserDTO updatedUser = createUserDTO(1L, updateUserDTO.getUsername(), updateUserDTO.getEmail());
        updatedUser.setUpdatedAt(Instant.now());

        when(userService.updateUser(eq(1L), any(UpdateUserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    UserDTO actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
                    assertUserDTOEquals(updatedUser, actualUser);
                });
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(eq(userId));

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(eq(userId));
    }
}
