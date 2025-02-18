package org.example.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {

    private Long id;
    private String content;
    private String userName;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private Instant createdAt;
    private Instant updatedAt;
}
