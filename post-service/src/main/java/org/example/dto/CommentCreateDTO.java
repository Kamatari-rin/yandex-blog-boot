package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentCreateDTO {

    @NotNull(message = "Content cannot be null")
    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Long parentCommentId;
}

