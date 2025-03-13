package org.example.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("post_tags")
public class PostTag {
    @Id
    private Long id;
    private Long postId;
    private Long tagId;
}