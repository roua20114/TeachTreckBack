package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if(optionalUserEntity.isEmpty()){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        UserEntity userEntity = optionalUserEntity.get();
        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.singletonList(userEntity.getRole()));
    }
}
