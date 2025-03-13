package org.example.model;

import lombok.*;
import org.example.enums.LikeTargetType;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("likes")
public class Like {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("target_id")
    private Long targetId;

    @Column("target_type_id")
    private Integer targetTypeValue;

    @Column("is_liked")
    private boolean liked;

    @Transient
    public LikeTargetType getTargetType() {
        return LikeTargetType.fromId(targetTypeValue);
    }

    public void setTargetType(LikeTargetType targetType) {
        this.targetTypeValue = targetType.getId();
    }
}
