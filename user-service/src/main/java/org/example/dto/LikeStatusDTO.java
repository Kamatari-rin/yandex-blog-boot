package org.example.dto;

import lombok.*;
import org.example.enums.LikeTargetType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeStatusDTO {
    private Long targetId;
    private LikeTargetType targetType;
    private boolean liked;
}
