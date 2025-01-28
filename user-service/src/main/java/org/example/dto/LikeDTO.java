package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.enums.LikeTargetType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {

    private Long id;
    private Long userId;

    @NotNull(message = "Target ID cannot be null")
    private Long targetId;

    @NotNull(message = "Target Type cannot be null")
    private LikeTargetType targetType;

    private boolean isLiked;
}