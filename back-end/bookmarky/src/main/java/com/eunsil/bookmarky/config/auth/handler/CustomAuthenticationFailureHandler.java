package com.eunsil.bookmarky.config.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드 = 401
        response.setContentType("text/plain;charset=UTF-8"); // 응답 값 타입
        response.getWriter().write("Authentication Failed");
        System.out.println("[Authentication Failed]: " + request.getParameter("username"));
    }
}
