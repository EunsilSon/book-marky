package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.domain.entity.SecureAnswer;
import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.user.SecureAnswerRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final SecureAnswerRepository secureAnswerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;
    private final SecurityUtil securityUtil;

    /**
     * 비밀번호 변경을 위한 토큰 생성 후 변경 링크를 메일로 전달
     */
    public boolean sendResetEmailWithToken(String username) {
        if (userRepository.existsByUsername(username)) {
            String uuid = resetTokenService.generateToken(username); // 일회용 토큰 생성
            return mailService.generateEmail(username, uuid); // 토큰을 포함한 메일 생성
        }
        return false;
    }


    /**
     * 토큰 유효성 검증 후 비밀번호 변경
     */
    @Transactional
    public boolean resetPwWithValidateToken(PasswordVO passwordVO) {
        if (resetTokenService.isValidToken(passwordVO.getToken())) {
            User user = userRepository.findByUsername(passwordVO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(passwordVO.getPassword()));
            userRepository.save(user);

            resetTokenService.invalidateToken(passwordVO.getToken());

            log.info("Reset password validated");
            return true;
        }
        return false;
    }


    /**
     * 보안 질문 검증
     * :비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     */
    public boolean checkSecureQuestion(SecureQuestionVO secureQuestionVO) {
        User user = userRepository.findByUsername(secureQuestionVO.getUsername());
        SecureAnswer secureAnswer = secureAnswerRepository.findBySecureQuestionIdAndUserId(
                userRepository.findByUsername(secureQuestionVO.getUsername()).getSecureQuestion().getId(), user.getId());
        return secureAnswer.getContent().equals(secureQuestionVO.getAnswer());
    }


    /**
     * 특정 사용자의 보안 질문 조회
     */
    public String getSecureQuestion(String username) {
        return userRepository.findByUsername(username).getSecureQuestion().getContent();
    }


    /**
     * 사용자의 보안 질문과 답변 등록
     */
    @Transactional
    public boolean registerSecureQuestion(User user, SecureQuestion question, String answerContent) {
        try {
            SecureAnswer answer = SecureAnswer.builder()
                    .secureQuestion(question)
                    .user(user)
                    .content(answerContent)
                    .build();
            secureAnswerRepository.save(answer);
            return true;
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }


    /**
     * 닉네임 조회
     */
    public String getNickname() {
        return userRepository.findByUsername(securityUtil.getCurrentUsername()).getNickname();
    }

}
