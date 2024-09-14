package com.TimeVenture.service.member.email.impl;

import com.TimeVenture.service.member.email.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private final static long TOKEN_VALIDITY = 180; // 3분

    //UUID를 통해 사용자에게 전달할 암호화 토큰을 생성
    @Override
    public String generateToken(String email) {
        // 간단한 토큰 생성 (실제 환경에서는 더 안전한 방법을 사용해야 합니다)
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(email, token, TOKEN_VALIDITY, TimeUnit.SECONDS);
        return token;
    }

    //전달한 UUID의 토큰을 redis서버의 토큰과 비교검증
    @Override
    public boolean checkToken(String email, String token) {
        String redisToken = redisTemplate.opsForValue().get(email);
        return token.equals(redisToken);
    }

}