package com.hotelapi.security;

import com.hotelapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        return User.withUsername(user.getEmail()).password(user.getPassword()).authorities("USER").build();
    }
}
