package org.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Builder.Default
    private Set<Long> likedPosts = new HashSet<>();

    @Builder.Default
    private Set<Long> dislikedPosts = new HashSet<>();

    @Builder.Default
    private Set<Long> posts = new HashSet<>();

    @Builder.Default
    private Set<Long> comments = new HashSet<>();

    private Instant createdAt;

    private Instant updatedAt;
}
