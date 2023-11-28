package com.example.usertemplate.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EXITS_EMAIL(HttpStatus.BAD_REQUEST,"이미 존재하는 이메일입니다"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST,"아이디 혹은 비밀번호를 확인하세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
