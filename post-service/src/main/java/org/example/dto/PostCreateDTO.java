package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {

    private Long userId;

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private String imageUrl;

    @NotNull
    @Size(min = 10)
    private String content;

    private Set<String> tags;
}
