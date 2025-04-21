package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.repositories.TagRepository;
import com.dilly.blog.services.TagService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

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
}
