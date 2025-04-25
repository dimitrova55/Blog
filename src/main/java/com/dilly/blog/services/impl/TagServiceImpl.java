package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.repositories.TagRepository;
import com.dilly.blog.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    /* GET all tags*/
    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

    /* POST create new tags */
    @Override
    @Transactional
    public List<Tag> createTags(Set<String> tagNames) {

        // get all tags from the database
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        // convert from 'list of Tag' to 'set of String'
        // 'set of String' containing only the tags' name
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName) // tag -> tag.getName()
                .collect(Collectors.toSet());

        // filter only the new tag names
        // build a tag object from each one
        // return a 'list of Tag'
        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();

        // if the newTags array is not empty,
        // save them into database
        if(!newTags.isEmpty()){
            savedTags = tagRepository.saveAll(newTags);
        }

        // combine the existing tags with the saved ones
        savedTags.addAll(existingTags);

        return savedTags;
    }

    /* DELETE an existing tag */
    @Override
    @Transactional
    public void deleteTag(UUID id) {
        // check if there are associated posts with this tag
        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isPresent()) {
            if(!tag.get().getPosts().isEmpty()){
                throw new IllegalStateException("Cannot delete tag with posts.");
            }
                tagRepository.deleteById(id);
        }
    }

    /* GET tag by its ID */
    @Override
    public Tag getTagById(UUID id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.orElseThrow(() -> new EntityNotFoundException("Tag not found."));
    }

    /* GET tags by theirs IDs */
    @Override
    public List<Tag> getTagByIds(Set<UUID> tagIds) {
        /**
         return tagIds.stream()
         .map(
         tagId -> tagRepository
         .findById(tagId)
         .orElseThrow(
         () -> new EntityNotFoundException("Tag not found.")
         )
         )

         .toList();
         **/

        List<Tag> foundTags = tagRepository.findAllById(tagIds);
        if (foundTags.size() != tagIds.size()) {
            throw new EntityNotFoundException("Not all tag IDs exist.");
        }

        return foundTags;
    }
}
