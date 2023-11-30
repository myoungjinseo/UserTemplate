package com.example.usertemplate.domain.user.repo;

import com.example.usertemplate.domain.user.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository{

    private final RedisTemplate redisTemplate;

    public void save(RefreshToken refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getEmail(),30,TimeUnit.DAYS);
        redisTemplate.expire(refreshToken.getRefreshToken(),30,TimeUnit.DAYS);
    }


}
