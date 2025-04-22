package com.dilly.blog.services;

import com.dilly.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
