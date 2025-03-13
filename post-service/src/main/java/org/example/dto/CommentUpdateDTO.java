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
public class CommentUpdateDTO {

    private Long id;
    @NotNull
    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    private Long userId;
}
