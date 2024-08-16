package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.domain.entity.SecureAnswer;
import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.PwQuestionVO;
import com.eunsil.bookmarky.domain.vo.PwResetVO;
import com.eunsil.bookmarky.domain.dto.PwResetDTO;
import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.SecureAnswerRepository;
import com.eunsil.bookmarky.repository.SecureQuestionRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SecureAnswerRepository secureAnswerRepository;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;
    private final SecureQuestionRepository secureQuestionRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       SecureAnswerRepository secureAnswerRepository,
                       MailService mailService,
                       ResetTokenService resetTokenService, SecureQuestionRepository secureQuestionRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.secureAnswerRepository = secureAnswerRepository;
        this.mailService = mailService;
        this.resetTokenService = resetTokenService;
        this.secureQuestionRepository = secureQuestionRepository;
    }

    /**
     * 회원가입
     * @param userVO 유저 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    @Transactional
    public boolean join(UserVO userVO) {
        if (!userRepository.existsByUsername(userVO.getUsername())) { // 유저 아이디 중복 체크

            User user = User.builder()
                    .username(userVO.getUsername())
                    .password(bCryptPasswordEncoder.encode(userVO.getPassword()))
                    .nickname(userVO.getNickname())
                    .telephone(userVO.getTelephone())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            // 보안 질문 생성
            SecureQuestion question = secureQuestionRepository.findById(userVO.getSecureQuestionId()).orElseThrow(null);
            SecureAnswer answer = SecureAnswer.builder()
                    .secureQuestion(question)
                    .user(user)
                    .content(userVO.getSecureAnswer())
                    .build();
            secureAnswerRepository.save(answer);

            return true;
        }
        return false;
    }


    /**
     * 비밀번호 변경을 위한 토큰 생성 후 변경 링크를 메일로 전달
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     */
    public ResponseEntity<PwResetDTO> sendResetEmailWithToken(String username) {

        if (userRepository.existsByUsername(username)) {
            String uuid = resetTokenService.generateToken(username); // 일회용 토큰 생성
            PwResetDTO pwResetDTO = new PwResetDTO(username, mailService.generateResetEmail(username, uuid)); // 토큰을 포함한 메일 생성

            return ResponseEntity.ok(pwResetDTO);
        }

        System.out.println("[Not Existed User]: " + username);
        return ResponseEntity.notFound().build(); // 404
    }


    /**
     * 토큰 유효성 검증 후 비밀번호 변경
     * @param pwResetVO 유저 이메일, 새 비밀번호, 토큰
     * @return 변경 여부
     */
    @Transactional
    public boolean resetPwWithTokenValidation(PwResetVO pwResetVO) {

        String token = pwResetVO.getToken();

        // 토큰 유효성 검사
        if (resetTokenService.isValidToken(token)) {

            // 비밀번호 재설정
            User user = userRepository.findByUsername(pwResetVO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(pwResetVO.getPassword()));
            userRepository.save(user);

            // 토큰 무효화
            resetTokenService.invalidateToken(token);
            System.out.println("[Password Reset Success]");

            return true;
        }

        System.out.println("[Password Reset Failed]: Invalid or expired token");
        return false;
    }


    /**
     * 보안 질문 검증
     * - 비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     *
     * @param pwQuestionVO 유저 메일, 답변
     * @return 저장된 답변과 일치 여부
     */
    public boolean checkSecureQuestion(PwQuestionVO pwQuestionVO) {
        User user = userRepository.findByUsername(pwQuestionVO.getUsername());
        SecureAnswer secureAnswer = secureAnswerRepository.findByUserId(user.getId());

        return secureAnswer.getContent().equals(pwQuestionVO.getAnswer());
    }


    /**
     * 사용자가 선택한 보안 질문 조회
     *
     * @param username 유저 메일
     * @return 질문
     */
    public String getSecureQuestion(String username) {
        User user = userRepository.findByUsername(username);
        return secureAnswerRepository.findByUserId(user.getId()).getSecureQuestion().getContent();
    }

}
