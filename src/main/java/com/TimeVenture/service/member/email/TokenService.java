package com.TimeVenture.service.member.email;

public interface TokenService {

    //UUID를 통해 사용자에게 전달할 암호화 토큰을 생성
    public String generateToken(String email);

    //전달한 UUID의 토큰을 redis서버의 토큰과 비교검증
    public boolean checkToken(String email, String token);
}
