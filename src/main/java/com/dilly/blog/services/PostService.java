package com.dilly.blog.services;

import com.dilly.blog.domain.entities.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {

    // find all posts by category id and/or tag id
    List<Post> findAllPosts(UUID categoryId, UUID tagId);
}
