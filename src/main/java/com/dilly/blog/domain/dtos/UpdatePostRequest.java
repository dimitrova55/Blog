package com.dilly.blog.domain.dtos;

import com.dilly.blog.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

    private UUID id;
    private String title;
    private String content;
    private UUID categoryId;

    @Builder.Default
    private Set<UUID> tagIds = new HashSet<>();

    private PostStatus status;
}
