package com.eunsil.bookmarky.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ResetTokenService {

    private static final String TOKEN_PREFIX = "password-reset-token: ";
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        String key = TOKEN_PREFIX + token;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, username, Duration.ofHours(24));

        return token;
    }

    public boolean isValidToken(String token) {
        String key = TOKEN_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Transactional
    public void invalidateToken(String token) {
        String key = TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }

}
