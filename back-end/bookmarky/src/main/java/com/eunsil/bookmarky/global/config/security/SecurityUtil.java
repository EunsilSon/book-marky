package com.eunsil.bookmarky.global.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    /**
     * 현재 Authentication 에 저장된 사용자의 username 반환
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return "anonymous";
        }
    }
}
