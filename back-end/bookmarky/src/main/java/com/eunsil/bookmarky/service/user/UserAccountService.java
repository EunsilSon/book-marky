package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.domain.entity.User;
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

    private final SecurityUtil securityUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;

    public boolean sendResetEmailWithToken(String username) {
        if (userRepository.existsByUsername(username)) {
            String token = generateToken(username);
            return sendResetEmail(username, token);
        }
        return false;
    }

    private String generateToken(String username) {
        return resetTokenService.generateToken(username);
    }

    private boolean sendResetEmail(String username, String token) {
        return mailService.generateEmail(username, token);
    }

    @Transactional
    public boolean resetPwWithToken(PasswordVO passwordVO) {
        if (isTokenValid(passwordVO.getToken())) {
            resetPassword(passwordVO.getUsername(), passwordVO.getPassword());
            inValidateToken(passwordVO.getToken());

            log.info("[{}] Reset Password Completed", passwordVO.getUsername());
            return true;
        }
        return false;
    }

    private boolean isTokenValid(String token) {
        return resetTokenService.isValidToken(token);
    }

    private void inValidateToken(String token) {
        resetTokenService.invalidateToken(token);
    }

    private void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username Not Found"));
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
    }

    public String getNickname() {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new RuntimeException("Username Not Found"));
        return user.getNickname();
    }

}
