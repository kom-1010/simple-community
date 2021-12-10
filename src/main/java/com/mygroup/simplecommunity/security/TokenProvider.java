package com.mygroup.simplecommunity.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {
    private static final String SECRET_KEY = "GJPDJ35DJctYDed9";

    public String create(String userId) {
        // 토큰 만료 기간은 6시간으로 설정
        Date expiryDate = Date.from(Instant.now().plus(6, ChronoUnit.HOURS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userId)
                .setIssuer("simple community")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }
}