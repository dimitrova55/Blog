package com.dilly.blog.services;

import com.dilly.blog.domain.dtos.CreatePostRequest;
import com.dilly.blog.domain.dtos.UpdatePostRequest;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    // find all posts by category id and/or tag id
    List<Post> findAllPosts(UUID categoryId, UUID tagId);

    List<Post> getDraftPost(User user);

    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID postId, UpdatePostRequest updatePostRequest);

    Post getPost(UUID postId);

    void deletePost(UUID postId);
}
