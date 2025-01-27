package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.global.config.security.SecurityUtil;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            String token = resetTokenService.generateToken(username);
            return mailService.generateEmail(username, token);
        }
        return false;
    }

    @Transactional
    public boolean resetPwWithToken(PasswordVO passwordVO) {
        String token = passwordVO.getToken();

        if (resetTokenService.isValidToken(token)) {
            resetPassword(passwordVO.getUsername(), passwordVO.getPassword());
            resetTokenService.invalidateToken(token);

            log.info("[{}] Reset Password Completed", passwordVO.getUsername());
            return true;
        }
        return false;
    }

    private void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
    }

    public String getNickname() {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        return user.getNickname();
    }

}
