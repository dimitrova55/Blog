package com.dilly.blog.repositories;

import com.dilly.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * FETCH ensures that the posts list is loaded immediately (it avoids the Lazy Loading problem).
     * LEFT JOIN means it will return all categories, even if some have no posts
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

}
