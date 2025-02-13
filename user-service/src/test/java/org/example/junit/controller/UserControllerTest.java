package org.example.junit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.junit.config.UserControllerTestConfig;
import org.example.controller.UserController;
import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserControllerTestConfig.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(result -> {
                    UserDTO actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
                    assertUserDTOEquals(newUser, actualUser);
                });
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO expectedUser = createUserDTO(1L, "existingUser", "existing@example.com");

        when(userService.getUserById(1L)).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?limit=10&offset=0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<UserDTO> actualUsers = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<List<UserDTO>>() {});
                    assertEquals(expectedUsers.size(), actualUsers.size(), "Размер списков не совпадает");
                    for (int i = 0; i < expectedUsers.size(); i++) {
                        assertUserDTOEquals(expectedUsers.get(i), actualUsers.get(i));
                    }
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    UserDTO actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
                    assertUserDTOEquals(updatedUser, actualUser);
                });
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(eq(userId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService, times(1)).deleteUser(eq(userId));
    }
}
