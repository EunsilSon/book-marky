package com.eunsil.bookmarky.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class ResetTokenService {

    private static final String TOKEN_PREFIX = "password-reset-token: ";
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ResetTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 토큰 생성 및 Redis 저장
     * @param username 로그인에 사용한 이메일
     * @return 토큰
     */
    @Transactional
    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        String key = TOKEN_PREFIX + token;

        // 토큰 저장 + 24시간 유효
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, username, Duration.ofHours(24));

        return token;
    }

    /**
     * 토큰 유효성 검사
     * @param token 토큰
     * @return 유효성 유무
     */
    public boolean isValidToken(String token) {
        String key = TOKEN_PREFIX + token; // TOKEN_PREFIX = "password-reset-token: "
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 토큰 무효화
     * @param token 토큰
     */
    @Transactional
    public void invalidateToken(String token) {
        String key = TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }

}
