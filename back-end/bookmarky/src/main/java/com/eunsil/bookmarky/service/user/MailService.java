package com.eunsil.bookmarky.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;
    @Value("${spring.props.reset-password-url}")
    private String RESET_PASSWORD_URL;

    private static final String URL_PARAM = "?token=";
    private static final String EMAIL_TITLE = "[북마키] 비밀번호 재설정 링크입니다.";
    private static final String RESET_PASSWORD_INSTRUCTIONS = "<b>아래 링크에 접속하여 비밀번호를 재설정 해주세요.</b><br><br>";
    private static final String EMAIL_EXPIRATION_NOTICE = "<br><br>해당 링크는 24시간 동안 유효하며, 1회 변경 가능합니다.<br>";
    private static final String FROM_NAME = "북마키";

    public boolean generateEmail(String username, String token) {
        String content = createEmailContent(token);
        return sendEmail(username, EMAIL_TITLE, content);
    }

    private String createEmailContent(String token) {
        return RESET_PASSWORD_INSTRUCTIONS +
                "<a href=\"" + RESET_PASSWORD_URL + URL_PARAM + token + "\"> "
                + RESET_PASSWORD_URL + URL_PARAM + token + "</a>"
                + EMAIL_EXPIRATION_NOTICE;
    }

    public boolean sendEmail(String username, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            helper.setTo(username);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);

            log.info("[{}] Send Password Reset Email Successfully", username);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send password reset email to " + username, e);
        }
    }
    
}