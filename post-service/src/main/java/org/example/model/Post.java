package org.example.model;

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
public class Post {

    private Long id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Content cannot be null")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    private String imageUrl;

    private String authorName;

    private Set<Tag> tags = new HashSet<>();

    private Long userId;

    private Instant createdAt;
    private Instant updatedAt;
}