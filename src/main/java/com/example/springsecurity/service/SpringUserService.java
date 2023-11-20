package com.example.springsecurity.service;

import com.example.springsecurity.model.SpringUser;
import com.example.springsecurity.model.SpringUserDetails;
import com.example.springsecurity.repository.SpringUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SpringUserService implements UserDetailsService {

    private final SpringUserRepository springUserRepository;

    public SpringUserService(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SpringUser springUser = springUserRepository.findByUsername(username);
        if (Objects.nonNull(springUser)) {
            return new SpringUserDetails(springUser);
        }
        throw new UsernameNotFoundException(username);
    }
    public UserDetails loadUserById(String id) throws Exception {
        SpringUser springUser = springUserRepository.findById(id);
        if (Objects.nonNull(springUser)) {
            return new SpringUserDetails(springUser);
        }
        throw new Exception("Could not find user wit id: %s".formatted(id));
    }

}

