package com.eunsil.bookmarky.config.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        response.setContentType("text/plain;charset=UTF-8"); // 응답 값 유형
        response.getWriter().write("Authentication Success");
        System.out.println("[Authentication Success]: " + request.getParameter("username"));
    }
}
