package com.dilly.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    /*
     * Tags and posts have independent lifecycles -
     * deleting a post shouldn't delete its tags,
     * and deleting a tag shouldn't delete associated posts.
     * This is why we don't specify cascade operations
     * in our relationship mappings.
     * Set instead of List for both sides of the relationship,
     * because Sets prevent duplicate associations between posts and tags.
     * One post cannot have the same tag more than once.
     * */
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

