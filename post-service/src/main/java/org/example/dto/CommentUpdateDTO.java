package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {

    @NotNull
    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    private Long userId;
    private Long postId;
    private Long parentCommentId;
}
