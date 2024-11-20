package com.eunsil.bookmarky.service.user;

import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecureQuestionService {

    private final UserRepository userRepository;

    public boolean checkQuestion(SecureQuestionVO secureQuestionVO) {
        User user = userRepository.findByUsername(secureQuestionVO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));
        return user.getSecureAnswer().equals(secureQuestionVO.getAnswer());
    }

    public String getQuestion(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));
        return user.getSecureQuestion().getContent();
    }
}
