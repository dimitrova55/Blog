package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.Tag;
import com.dilly.blog.repositories.TagRepository;
import com.dilly.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }
}
