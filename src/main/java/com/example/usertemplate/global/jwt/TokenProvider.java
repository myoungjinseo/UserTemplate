package com.example.usertemplate.global.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private final String secret;
    private SecretKey secretKey;

    public TokenProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }


    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, Long time) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("email",email);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+time))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }





    public String getUserEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
