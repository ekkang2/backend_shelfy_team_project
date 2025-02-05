package com.shelfy.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Log4j2
@Component
public class JwtProvider {

    private final String secret = "U8Khnm7bYZMJJFJTZKy8rxDE8b18zmah";
    private final long expirationMs = 3600000;

    // 비밀 키를 byte 배열로 변환하여 Key 객체 생성
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    // Jwt 토큰 생성
    public String generateToken(String userUid) {
        // 현재 시간과 만료 시간 설정
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userUid)
//                .claim("userUid", userUid)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    // 전달 받은 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
//            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            // parserBuilder()를 통해 토큰의 서명을 검증합니다
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (Exception e) {
            log.error(e);
        }
        return false;
    }

    // 토큰에서 사용자 이름을 추출
    public String getUserUidFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

}
