package com.example.board.user.service;

import com.example.board.common.entity.BaseEntity;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsernameAndState(username, BaseEntity.State.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException(username));
    }
}
