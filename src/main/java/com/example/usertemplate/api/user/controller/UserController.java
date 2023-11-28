package com.example.usertemplate.api.user.controller;

import com.example.usertemplate.api.user.dto.request.UserRequest;
import com.example.usertemplate.api.user.dto.response.AccountResponse;
import com.example.usertemplate.api.user.dto.response.TokenResponse;
import com.example.usertemplate.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public AccountResponse signUp(@RequestBody UserRequest userRequest) {
        AccountResponse response = userService.signUp(userRequest);
        return response;
    }

    @PostMapping("/signIn")
    public TokenResponse signIn(@RequestBody UserRequest userRequest) {
        TokenResponse response = userService.signIn(userRequest);
        return response;
    }
}
