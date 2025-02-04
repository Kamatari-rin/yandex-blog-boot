package org.example.model;

import lombok.*;
import org.example.enums.LikeTargetType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    private Long id;
    private Long userId;
    private Long targetId;
    private LikeTargetType targetType;
    private boolean liked;
}