package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.domain.entity.SecureAnswer;
import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.repository.user.SecureAnswerRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecureQuestionService {

    private final UserRepository userRepository;
    private final SecureAnswerRepository secureAnswerRepository;

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

    public boolean checkSecureQuestion(SecureQuestionVO secureQuestionVO) {
        User user = userRepository.findByUsername(secureQuestionVO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        SecureAnswer secureAnswer = secureAnswerRepository.findBySecureQuestionIdAndUserId(user.getSecureQuestion().getId(), user.getId());
        return secureAnswer.getContent().equals(secureQuestionVO.getAnswer());
    }

    public String getSecureQuestion(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        return user.getSecureQuestion().getContent();
    }
}
