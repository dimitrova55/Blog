package com.dilly.blog.services.impl;

import com.dilly.blog.domain.PostStatus;
import com.dilly.blog.domain.dtos.CreatePostRequest;
import com.dilly.blog.domain.dtos.UpdatePostRequest;
import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.domain.entities.User;
import com.dilly.blog.repositories.CategoryRepository;
import com.dilly.blog.repositories.PostRepository;
import com.dilly.blog.services.CategoryService;
import com.dilly.blog.services.PostService;
import com.dilly.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    /** GET all posts **/
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

    /** GET Draft post **/
    @Override
    public List<Post> getDraftPost(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    /** POST create a post **/
    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {

        // create new object of type Post and populate it
        Post newPost = new Post();

        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID postId, UpdatePostRequest updatePostRequest) {

        // check if the post exists in the database
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + "does not exist."));

        // update the current post's information
        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        // check if the current category ID equals the updated category ID
        // if they don't update the post's category
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        // get the current post's tags
        Set<UUID> existingTagIds = existingPost.getTags()
                .stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toSet());

        // get the updated post's tags
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();

        // compare them
        if(!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new EntityNotFoundException("The post cannot be found.")
                );
    }


    /* DELETE post by ID */
    @Override
    @Transactional
    public void deletePost(UUID postId) {
        Post post = getPost(postId);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
