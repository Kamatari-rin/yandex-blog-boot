package org.example.service;

import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers(int limit, int offset);
    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO);
    void deleteUser(Long id);
}

