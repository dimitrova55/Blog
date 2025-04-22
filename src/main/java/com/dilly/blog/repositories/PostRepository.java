package com.dilly.blog.repositories;

import com.dilly.blog.domain.PostStatus;
import com.dilly.blog.domain.entities.Category;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
