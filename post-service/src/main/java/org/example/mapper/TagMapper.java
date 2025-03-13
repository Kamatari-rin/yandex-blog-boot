package org.example.mapper;

import org.example.model.Tag;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {

    default String tagToString(Tag tag) {
        return tag != null ? tag.getName() : null;
    }

    default Tag stringToTag(String name) {
        return name != null ? Tag.builder().name(name).build() : null;
    }

    default Set<String> tagsToStrings(Set<Tag> tags) {
        return tags != null
                ? tags.stream().map(this::tagToString).collect(Collectors.toSet())
                : null;
    }

    default Set<Tag> stringsToTags(Set<String> tagNames) {
        return tagNames != null
                ? tagNames.stream().map(this::stringToTag).collect(Collectors.toSet())
                : null;
    }
}
