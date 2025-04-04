package com.dilly.blog.mappers;

import com.dilly.blog.domain.PostStatus;
import com.dilly.blog.domain.dtos.CreateCategoryRequest;
import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.domain.dtos.CategoryDto;
import com.dilly.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;



/**
 * defines how to convert between Category entities and CategoryDto objects
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    /*
    * from Entity --> to Data Transfer Object *
    */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(posts == null){
            return 0;
        }
        return posts
                .stream()
                .filter(
                        post -> PostStatus.PUBLISHED.equals(post.getStatus())
                )
                .count();
    }

    /*
     * From Request Object --> to Entity *
     */
    Category toEntity(CreateCategoryRequest createCategoryRequest);

}
