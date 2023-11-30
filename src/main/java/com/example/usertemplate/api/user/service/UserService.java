package com.example.usertemplate.api.user.service;

import com.example.usertemplate.api.user.dto.request.UserRequest;
import com.example.usertemplate.api.user.dto.response.AccountResponse;
import com.example.usertemplate.api.user.dto.response.TokenResponse;
import com.example.usertemplate.domain.user.entity.RefreshToken;
import com.example.usertemplate.domain.user.entity.Role;
import com.example.usertemplate.domain.user.entity.User;
import com.example.usertemplate.domain.user.repo.RefreshTokenRepository;
import com.example.usertemplate.domain.user.repo.UserRepository;
import com.example.usertemplate.global.exception.ErrorCode;
import com.example.usertemplate.global.exception.ErrorException;
import com.example.usertemplate.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.token-milliseconds.atk}")
    private Long atkTime;
    @Value("${jwt.token-milliseconds.rtk}")
    private Long rtkTime;

    @Transactional
    public AccountResponse signUp(UserRequest userRequest) {
        boolean isExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isExist) {
            throw new ErrorException(ErrorCode.EXITS_EMAIL);
        }
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();


        userRepository.save(user);
        return AccountResponse.of(user);
    }

    @Transactional
    public TokenResponse signIn(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_FAIL));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new ErrorException(ErrorCode.LOGIN_FAIL);
        }

        String atk = tokenProvider.createToken(userRequest.getEmail(), atkTime);
        String rtk = tokenProvider.createToken(userRequest.getEmail(), rtkTime);

        RefreshToken refreshToken = new RefreshToken(rtk, user.getEmail(), LocalDateTime.now().plusSeconds(rtkTime / 1000));
        refreshTokenRepository.save(refreshToken);
        return new TokenResponse(atk, rtk);

    }
}
