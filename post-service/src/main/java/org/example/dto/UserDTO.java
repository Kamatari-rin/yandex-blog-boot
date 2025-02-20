package org.example.dto;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<Long> likedPosts;
    private Set<Long> dislikedPosts;
}
