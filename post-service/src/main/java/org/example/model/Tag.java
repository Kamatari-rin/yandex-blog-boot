package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag {

    private Long id;

    @NotNull(message = "Tag name cannot be null")
    @Size(min = 1, max = 50, message = "Tag name must be between 1 and 50 characters")
    private String name;

    @JsonCreator
    public static Tag fromString(String name) {
        return Tag.builder().name(name).build();
    }
}
