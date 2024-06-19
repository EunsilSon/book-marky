package com.eunsil.bookmarky.config.auth;

import com.eunsil.bookmarky.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Security 로그인
     * @param username 로그인에 사용되는 email 타입
     * @return UserDetails 타입의 User 정보
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.existsByUsername(username)) {
            return new CustomUserDetails(userRepository.findByUsername(username));
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
