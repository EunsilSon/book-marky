package com.eunsil.bookmarky.config.auth;

import com.eunsil.bookmarky.domain.dto.UserDTO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptpasswordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder bCryptpasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptpasswordEncoder = bCryptpasswordEncoder;
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


    public String join(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(bCryptpasswordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
        return "ok";
    }
}
