package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.repository.user.SecureQuestionRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRegistrationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SecureQuestionRepository secureQuestionRepository;
    private final SecureQuestionService secureQuestionService;

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
            throw new DuplicateRequestException("Username already exists.");
        }

        if (isDuplicateNickname(userVO.getNickname())) {
            throw new DuplicateRequestException("Nickname already exists.");
        }

        if (isDuplicateTelephone(userVO.getTelephone())) {
            throw new DuplicateRequestException("Telephone already exists.");
        }

        try {
            // User 생성
            SecureQuestion secureQuestion = secureQuestionRepository.findById(userVO.getSecureQuestionId()).orElseThrow(null);

            User user = User.builder()
                    .username(userVO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userVO.getPassword()))
                    .nickname(userVO.getNickname())
                    .telephone(userVO.getTelephone())
                    .secureQuestion(secureQuestion)
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);

            // 보안 질문 생성
            return secureQuestionService.registerSecureQuestion(user, secureQuestion, userVO.getAnswerContent());
        } catch(Exception e) {
            log.warn("[{}] User Registration Rolled Back.", userVO.getUsername());
            return false;
        }
    }

}
