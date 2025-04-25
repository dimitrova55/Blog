package com.dilly.blog.domain.entities;

import com.dilly.blog.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PostStatus status;

    @Column(nullable = false)
    private Integer readingTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /*
    * One User can write many posts.
    * Each post has only one User (author).
    *
    * FetchType.LAZY - load author details
    * only when needed
    *
    * */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id", nullable = false)
    private User author;

    /*
     * Many posts can be from the same category.
     * Each post in our blog system
     * must belong to exactly one category,
     * establishing a one-to-many relationship.
     *
     * Posts and categories have independent lifecycles
     * Deleting a category shouldn't automatically delete its posts,
     * as they might need to be reassigned to another category.
     *
     * FetchType.LAZY - load category details
     * only when needed
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /*
    * Tags and posts have independent lifecycles -
    * deleting a post shouldn't delete its tags,
    * and deleting a tag shouldn't delete associated posts.
    * This is why we don't specify cascade operations
    * in our relationship mappings.
    * Set instead of List for both sides of the relationship,
    * because Sets prevent duplicate associations between posts and tags.
    * */
    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(title, post.title) &&
                Objects.equals(content, post.content) &&
                status == post.status &&
                Objects.equals(readingTime, post.readingTime) &&
                Objects.equals(createdAt, post.createdAt) &&
                Objects.equals(updatedAt, post.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, readingTime,
                                        createdAt, updatedAt);
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
