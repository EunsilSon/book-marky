package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.domain.entity.SecureAnswer;
import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.domain.dto.PasswordDTO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.user.SecureAnswerRepository;
import com.eunsil.bookmarky.repository.user.SecureQuestionRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final SecureAnswerRepository secureAnswerRepository;
    private final SecureQuestionRepository secureQuestionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;
    private final SecurityUtil securityUtil;

    /**
     * 비밀번호 변경을 위한 토큰 생성 후 변경 링크를 메일로 전달
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     */
    public ResponseEntity<PasswordDTO> sendResetEmailWithToken(String username) {

        if (userRepository.existsByUsername(username)) {
            String uuid = resetTokenService.generateToken(username); // 일회용 토큰 생성
            PasswordDTO passwordDTO = new PasswordDTO(username, mailService.generateResetEmail(username, uuid)); // 토큰을 포함한 메일 생성

            return ResponseEntity.ok(passwordDTO);
        }

        return ResponseEntity.notFound().build(); // 404
    }


    /**
     * 토큰 유효성 검증 후 비밀번호 변경
     * @param passwordVO 유저 이메일, 새 비밀번호, 토큰
     * @return 변경 여부
     */
    @Transactional
    public boolean resetPwWithTokenValidation(PasswordVO passwordVO) {

        String token = passwordVO.getToken();

        // 토큰 유효성 검사
        if (resetTokenService.isValidToken(token)) {

            // 비밀번호 재설정
            User user = userRepository.findByUsername(passwordVO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(passwordVO.getPassword()));
            userRepository.save(user);

            // 토큰 무효화
            resetTokenService.invalidateToken(token);
            return true;
        }
        return false;
    }


    /**
     * 보안 질문 검증
     * - 비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     *
     * @param secureQuestionVO 유저 메일, 답변
     * @return 저장된 답변과 일치 여부
     */
    public boolean checkSecureQuestion(SecureQuestionVO secureQuestionVO) {
        User user = userRepository.findByUsername(secureQuestionVO.getUsername());
        SecureAnswer secureAnswer = secureAnswerRepository.findBySecureQuestionIdAndUserId(secureQuestionVO.getSecureQuestionId(), user.getId());

        return secureAnswer.getContent().equals(secureQuestionVO.getAnswer());
    }


    /**
     * 보안 질문 조회
     *
     * @return SecureQuestionId 리스트
     */
    public List<SecureQuestion> getSecureQuestion() {
        return secureQuestionRepository.findAll();
    }


    /**
     * 사용자의 보안 질문과 답변 등록
     */
    @Transactional
    public boolean registerSecureQuestion(User user, Long questionId, String answerContent) {
        try {
            SecureQuestion question = secureQuestionRepository.findById(questionId).orElseThrow(null);
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


    public String getNickname() {
        return userRepository.findByUsername(securityUtil.getCurrentUsername()).getNickname();
    }

}