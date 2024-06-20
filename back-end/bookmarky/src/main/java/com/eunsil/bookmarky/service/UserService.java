package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.dto.PasswordResetReq;
import com.eunsil.bookmarky.domain.dto.PasswordResetRes;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       MailService mailService,
                       ResetTokenService resetTokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.resetTokenService = resetTokenService;
    }

    /**
     * 회원가입
     * @param userDTO 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    public boolean join(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())) { // 유저 아이디 중복 체크
            User user = User.builder()
                    .username(userDTO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                    .nickname(userDTO.getNickname())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
            return true;
        }
        return false;
    }


    /**
     * 비밀번호 변경 메일 생성 및 전송
     * @param username
     * @return 유저 이름, 일회용 토큰
     * @throws Exception 존재하지 않는 사용자
     */
    @Transactional
    public PasswordResetRes sendResetEmail(String username) throws Exception {

        if (userRepository.existsByUsername(username)) {
            String uuid = mailService.generateResetEmail(username); // 메일 생성 및 전송
            return new PasswordResetRes(username, uuid);
        }

        System.out.println("[Not Existed User]: " + username);
        throw new Exception("Not Existed User");
    }

}
