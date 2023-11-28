package com.example.usertemplate.global.jwt;

import com.example.usertemplate.api.user.service.UserDetailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private SecretKey secretKey;


    private final UserDetailService userDetailService;


    @PostConstruct
    protected void init(@Value("${jwt.secret}")
                            String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
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



    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailService.loadUserByUsername(getUserEmail(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
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
