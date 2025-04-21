package com.dilly.blog.mappers;

import com.dilly.blog.domain.PostStatus;
import com.dilly.blog.domain.dtos.TagResponse;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    // Tag -> TagResponse
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(Tag tag);

    // List<Tag> -> List<TagResponse>
    List<TagResponse> toTagResponseList(List<Tag> tags);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts){
        if(posts == null){
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
