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

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRegistrationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SecureQuestionRepository secureQuestionRepository;

    public boolean isUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isTelephoneDuplicate(String telephone) {
        return userRepository.existsByTelephone(telephone);
    }

    private void checkUserDuplication(UserVO userVO) {
        if (isUsernameDuplicate(userVO.getUsername())) {
            throw new DuplicateRequestException("Username already exists.");
        }
        if (isNicknameDuplicate(userVO.getNickname())) {
            throw new DuplicateRequestException("Nickname already exists.");
        }
        if (isTelephoneDuplicate(userVO.getTelephone())) {
            throw new DuplicateRequestException("Telephone already exists.");
        }
    }

    private void createUser(UserVO userVO, SecureQuestion secureQuestion) {
        User user = User.builder()
                .username(userVO.getUsername())
                .password(bCryptPasswordEncoder.encode(userVO.getPassword()))
                .nickname(userVO.getNickname())
                .telephone(userVO.getTelephone())
                .secureQuestion(secureQuestion)
                .secureAnswer(userVO.getAnswerContent())
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }

    @Transactional
    public boolean registerUser(UserVO userVO) {
        try {
            checkUserDuplication(userVO);

            SecureQuestion secureQuestion = secureQuestionRepository.findById(userVO.getSecureQuestionId())
                    .orElseThrow(() -> new NoSuchElementException("Secure Question Not Found."));

            createUser(userVO, secureQuestion);
            return true;
        } catch(NoSuchElementException e) {
            log.warn("[{}] Secure question with ID '{}' not found.", userVO.getUsername(), userVO.getSecureQuestionId());
            return false;
        } catch(Exception e) {
            log.warn("[{}] {}", userVO.getUsername(), e.getMessage());
            return false;
        }
    }

}
