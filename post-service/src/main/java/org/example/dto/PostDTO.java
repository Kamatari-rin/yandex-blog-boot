package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PostDTO {

    private Long id;
    private Long userId;
    private String title;
    private String imageUrl;
    private String content;
    private String authorName;

    private int commentsCount;
    private int likesCount;

    private Set<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
