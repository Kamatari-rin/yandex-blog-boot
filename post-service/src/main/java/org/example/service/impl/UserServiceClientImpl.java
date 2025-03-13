package org.example.service.impl;

import org.example.dto.UserDTO;
import org.example.enums.LikeTargetType;
import org.example.service.UserServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClientImpl implements UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDTO fetchUserById(Long userId) {
        if (userServiceUrl == null || userServiceUrl.isBlank()) {
            throw new RuntimeException("user.service.url is not set in application.properties");
        }

        String userUrl = String.format("%s/api/users/%d", userServiceUrl, userId);

        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity(userUrl, UserDTO.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new RuntimeException("Failed to fetch user information for user ID: " + userId);
        }

        return responseEntity.getBody();
    }

    @Override
    public int fetchLikesCountByTarget(Long targetId, LikeTargetType targetType) {
        String likesCountUrl = String.format("%s/api/likes/count?targetId=%d&targetType=%s",
                userServiceUrl, targetId, targetType.name());

        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(likesCountUrl, Integer.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new RuntimeException("Failed to fetch like count for target ID: " + targetId + " and target type: " + targetType);
        }

        return responseEntity.getBody();
    }
}