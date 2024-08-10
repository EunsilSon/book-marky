package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.request.PwResetReq;
import com.eunsil.bookmarky.domain.response.PwResetRes;
import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            return true;
        }
        return false;
    }


    /**
     * 비밀번호 변경을 위한 토큰 생성 후 변경 링크를 메일로 전달
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     */
    @Transactional
    public ResponseEntity<PwResetRes> sendResetEmailWithToken(String username) {

        if (userRepository.existsByUsername(username)) {
            String uuid = resetTokenService.generateToken(username); // 일회용 토큰 생성
            PwResetRes pwResetRes = new PwResetRes(username, mailService.generateResetEmail(username, uuid)); // 토큰을 포함한 메일 생성

            return ResponseEntity.ok(pwResetRes);
        }

        System.out.println("[Not Existed User]: " + username);
        return ResponseEntity.notFound().build(); // 404
    }


    /**
     * 토큰 유효성 검증 후 비밀번호 변경
     * @param pwResetReq 유저 이메일, 새 비밀번호, 토큰
     * @return 변경 여부
     */
    @Transactional
    public boolean resetPwWithTokenValidation(PwResetReq pwResetReq) {

        String token = pwResetReq.getToken();

        // 토큰 유효성 검사
        if (resetTokenService.isValidToken(token)) {

            // 비밀번호 재설정
            User user = userRepository.findByUsername(pwResetReq.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(pwResetReq.getPassword()));
            userRepository.save(user);

            // 토큰 무효화
            resetTokenService.invalidateToken(token);
            System.out.println("[Password Reset Success]");

            return true;
        }

        System.out.println("[Password Reset Failed]: Invalid or expired token");
        return false;
    }

}
