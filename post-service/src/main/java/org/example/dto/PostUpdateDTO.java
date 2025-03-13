package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.model.Tag;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDTO {

    private Long postId;
    private Long userId;

    @NotNull(message = "Title cannot be null")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Content cannot be null")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    private String imageUrl;
    private Set<String> tags;
}
