package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserAccountService userAccountService;

    public boolean isDuplicateUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isDuplicateTelephone(String telephone) {
        return userRepository.existsByTelephone(telephone);
    }

    @Transactional
    public boolean registerUser(UserVO userVO) {

        if (isDuplicateUsername(userVO.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (isDuplicateNickname(userVO.getNickname())) {
            throw new RuntimeException("Nickname is already taken");
        }

        if (isDuplicateTelephone(userVO.getTelephone())) {
            throw new RuntimeException("Telephone is already taken");
        }

        try {
            // 유저 등록
            User user = User.builder()
                    .username(userVO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userVO.getPassword()))
                    .nickname(userVO.getNickname())
                    .telephone(userVO.getTelephone())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);

            // 보안 질문 등록 (실패 시 롤백)
            return userAccountService.registerSecureQuestion(user, userVO.getSecureQuestionId(), userVO.getAnswerContent());
        } catch(Exception e) {
            log.info("[{}] User registration has been rolled back.", userVO.getUsername());
            return false;
        }
    }

}
