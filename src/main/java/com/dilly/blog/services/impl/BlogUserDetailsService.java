package com.dilly.blog.services.impl;

import com.dilly.blog.domain.entities.User;
import com.dilly.blog.repositories.UserRepository;
import com.dilly.blog.security.BlogUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         * userRepository.findByEmail() returns Optional<User>
         * orElseThrow() is method part of the Optional class
         */
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new BlogUserDetails(user);
    }
}
