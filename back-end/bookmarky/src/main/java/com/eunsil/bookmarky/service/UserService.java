package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.dto.UserDTO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 회원가입
     * @param userDTO 유저 이름, 비밀번호, 닉네임
     * @return 성공 여부
     */
    public String join(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())) { // 유저 아이디 중복 체크
            User user = User.builder()
                    .username(userDTO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                    .nickname(userDTO.getNickname())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
            return "ok";
        }
        return "fail";
    }


}
