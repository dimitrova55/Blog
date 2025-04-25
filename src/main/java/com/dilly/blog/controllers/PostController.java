package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.*;
import com.dilly.blog.domain.entities.Post;
import com.dilly.blog.domain.entities.User;
import com.dilly.blog.mappers.PostMapper;
import com.dilly.blog.services.PostService;
import com.dilly.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    /* GET All posts */
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

    /* GET All draft posts */
    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){

        // find the user by his id
        User loggedInUser = userService.getUserById(userId);

        // get all the user's draft posts
        List<Post> draftPost = postService.getDraftPost(loggedInUser);

        // convert the Post data to PostDto
        List<PostDto> postDtos = draftPost.stream()
                .map(post -> postMapper.toDto(post))
                .toList();

        return ResponseEntity.ok(postDtos);
    }

    /* POST create new post */
    @PostMapping
    public ResponseEntity<PostDto> CreatePost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            )
    {
        // get the currently logged user
        User loggedUser = userService.getUserById(userId);

        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        // create a new post
        Post createdPost = postService.createPost(loggedUser, createPostRequest);

        // convert the Post variable to PostDto
        PostDto createdPostDto = postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    /* PUT update post */
    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID postId,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
            )
    {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(postId, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    /* GET post by ID */
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID postId)
    {
        Post post = postService.getPost(postId);
        PostDto postDto = postMapper.toDto(post);

        return ResponseEntity.ok(postDto);
    }

    /* DELETE post by ID */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
