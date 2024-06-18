package com.eunsil.bookmarky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login").authenticated() // 인증이 필요한 주소
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginProcessingUrl("/login") // Security 가 로그인을 대신 수행하므로 컨트롤러를 만들지 않아도 됨
                        .defaultSuccessUrl("/home") // 로그인 성공 후 이동 주소
                        .permitAll())
                .build();
    }
}