package com.eunsil.bookmarky.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.props.reset-password-url}")
    private String resetPwUrl;

    private final JavaMailSender mailSender;


    public String generateEmail(String username, String uuid) {
        String title = "[북마키] 비밀번호 재설정 링크입니다.";
        String content = "아래 링크에 접속하여 비밀번호를 재설정 해주세요.<br><br>"
                + "<a href=\"" + resetPwUrl + "/" + uuid + "\"> "
                + resetPwUrl + "/" + uuid + "</a>"
                + "<br><br>해당 링크는 24시간 동안 유효하며, 1회 변경 가능합니다.<br>"; // 임시 주소

        sendEmail(username, title, content);
        return uuid;
    }


    public void sendEmail(String username, String title, String content) {
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

