package com.dilly.blog.services.impl;

import com.dilly.blog.domain.PostStatus;
import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.repositories.CategoryRepository;
import com.dilly.blog.repositories.PostRepository;
import com.dilly.blog.services.CategoryService;
import com.dilly.blog.services.PostService;
import com.dilly.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAllPosts(UUID categoryId, UUID tagId) {

        if(categoryId != null && tagId != null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED, category, tag);
        }

        // if tagId is null
        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        // if categoryId is null
        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED, tag);
        }

        // if both are null
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }
}
