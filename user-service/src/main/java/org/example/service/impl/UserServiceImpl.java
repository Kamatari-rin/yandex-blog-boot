package org.example.service.impl;

import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.dto.UserDTO;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = getUserByIdOrThrow(id);
        return userMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers(int limit, int offset) {
        return userRepository.findAll(limit, offset)
                .stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = userMapper.toEntity(createUserDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = getUserByIdOrThrow(id);

        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());

        User updatedUser = userRepository.update(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
