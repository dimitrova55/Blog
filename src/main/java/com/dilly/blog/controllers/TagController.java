package com.dilly.blog.controllers;

import com.dilly.blog.domain.dtos.CreateTagsRequest;
import com.dilly.blog.domain.dtos.TagResponse;
import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.mappers.TagMapper;
import com.dilly.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    // display all existing tags
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tagMapper.toTagResponseList(tags));
    }

    // create new Tag list
    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags (@RequestBody CreateTagsRequest createTagsRequest){

        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());
        List<TagResponse> createTagResponse = savedTags.stream()
                .map(tagMapper::toTagResponse) // (tag -> tagMapper.toTagResponse(tag)
                .toList();

        return new ResponseEntity<>(createTagResponse, HttpStatus.CREATED);
    }

    // delete an existing tag
    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
