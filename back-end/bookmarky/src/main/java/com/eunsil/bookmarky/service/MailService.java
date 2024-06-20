package com.eunsil.bookmarky.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    private final ResetTokenService resetTokenService;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.props.reset-password-url}")
    private String resetPwUrl;

    @Autowired
    public MailService(JavaMailSender mailSender, ResetTokenService resetTokenService) {
        this.mailSender = mailSender;
        this.resetTokenService = resetTokenService;
    }

    /**
     * 이메일 생성
     * @param username
     * @return
     */
    @Transactional
    public String generateResetEmail(String username) {
        String uuid = resetTokenService.generateToken(username); // 일회용 토큰 생성

        String title = "[북마키] 비밀번호 재설정 링크입니다.";
        String content = "아래 링크에 접속하여 비밀번호를 재설정 해주세요.<br>"
                + "<a href=\"" + resetPwUrl + "/" + uuid + "\"> "
                + resetPwUrl + "/" + uuid + "</a>"
                + "<br><br>해당 링크는 24시간 동안 유효하며, 1회 변경 가능합니다.<br>"; // 임시 주소

        sendMail(username, title, content);
        return uuid;
    }

    /**
     * 이메일 전송
     * @param username
     * @param title
     * @param content
     */
    public void sendMail(String username, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(new InternetAddress(fromEmail, "북마키"));
            helper.setTo(username);
            helper.setSubject(title);
            helper.setText(content, true); // true=html
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}

