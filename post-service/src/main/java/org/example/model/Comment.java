package org.example.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("comments")
public class Comment implements Persistable<Long> {
    @Id
    private Long id;

    @NotNull(message = "Content cannot be null")
    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    private Long postId;
    private Long userId;

    private Long parentCommentId;

    @ReadOnlyProperty
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
