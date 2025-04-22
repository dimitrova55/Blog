package com.dilly.blog.mappers;

import com.dilly.blog.domain.dtos.CreatePostRequest;
import com.dilly.blog.domain.dtos.CreatePostRequestDto;
import com.dilly.blog.domain.dtos.PostDto;
import com.dilly.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    // Post -> PostDto
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
}
