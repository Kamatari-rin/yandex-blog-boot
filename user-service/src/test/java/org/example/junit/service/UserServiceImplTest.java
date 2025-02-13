package org.example.junit.service;

import org.example.junit.config.UserServiceTestConfig;
import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceTestConfig.class)
@ActiveProfiles("test")
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reset(userRepository);
    }

    private User createUser(Long id, String username, String email, String password) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
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
        assertNotNull(actual, "Результат не должен быть null");
        assertEquals(expected.getId(), actual.getId(), "ID пользователя не совпадает");
        assertEquals(expected.getUsername(), actual.getUsername(), "Имя пользователя не совпадает");
        assertEquals(expected.getEmail(), actual.getEmail(), "Email пользователя не совпадает");
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt(), "Время создания пользователя не совпадает");
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt(), "Время обновления пользователя не совпадает");
    }

    @Test
    void testContext() {
        System.out.println("### ApplicationContext: " + context.getClass().getName());
    }

    @Test
    void testGetUserById_Success() {
        Long userId = 1L;
        User user = createUser(userId, "testUser", "test@example.com", "password");
        UserDTO expectedUserDTO = createUserDTO(userId, "testUser", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(expectedUserDTO);

        UserDTO result = userService.getUserById(userId);
        assertUserDTOEquals(expectedUserDTO, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("User not found with id: 1", exception.getMessage(), "Сообщение исключения не совпадает для userId: " + userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAllUsers() {
        User user = createUser(1L, "testUser", "test@example.com", "password");
        UserDTO userDTO = createUserDTO(1L, "testUser", "test@example.com");

        when(userRepository.findAll(10, 0)).thenReturn(List.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers(10, 0);
        assertNotNull(result, "Результат не должен быть null");
        assertEquals(1, result.size(), "Количество пользователей не совпадает");
        assertUserDTOEquals(userDTO, result.get(0));
        verify(userRepository, times(1)).findAll(10, 0);
    }

    @Test
    void testCreateUser() {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .username("newUser")
                .email("newuser@example.com")
                .password("password123")
                .build();

        User newUser = createUser(2L, "newUser", "newuser@example.com", "password123");
        UserDTO newUserDTO = createUserDTO(2L, "newUser", "newuser@example.com");

        when(userMapper.toEntity(createUserDTO)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toUserDTO(newUser)).thenReturn(newUserDTO);

        UserDTO result = userService.createUser(createUserDTO);
        assertUserDTOEquals(newUserDTO, result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testUpdateUser() {
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .username("updatedUser")
                .email("updateduser@example.com")
                .build();

        User existingUser = createUser(1L, "testUser", "test@example.com", "password123");
        User updatedUser = createUser(1L, "updatedUser", "updateduser@example.com", "password123");
        UserDTO updatedUserDTO = createUserDTO(1L, "updatedUser", "updateduser@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.update(existingUser)).thenReturn(updatedUser);
        when(userMapper.toUserDTO(updatedUser)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateUser(1L, updateUserDTO);
        assertUserDTOEquals(updatedUserDTO, result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).update(existingUser);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).delete(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(1L);
    }
}