package com.dilly.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String username, String password);
    String generateToken(UserDetails userDetails);
}
