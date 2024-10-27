package com.eunsil.bookmarky.config.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.core.Authentication;

@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
