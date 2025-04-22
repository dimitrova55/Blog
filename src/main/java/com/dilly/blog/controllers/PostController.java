package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.PostDto;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.mappers.PostMapper;
import com.dilly.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required=false) UUID categoryId,
            @RequestParam(required = false) UUID tagId)
    {
        List<Post> posts = postService.findAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream()
                .map(postMapper::toDto) //post -> postMapper.toDto(post)
                .toList();
        return ResponseEntity.ok(postDtos);
    }
}
