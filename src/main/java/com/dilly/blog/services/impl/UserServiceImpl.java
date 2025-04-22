package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.User;
import com.dilly.blog.repositories.UserRepository;
import com.dilly.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() ->
                new EntityNotFoundException("The user with id: " + id + "cannot be found."));
    }
}
