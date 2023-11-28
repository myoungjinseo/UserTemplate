package com.example.usertemplate.api.user.dto.response;

import com.example.usertemplate.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountResponse {
    private Long userId;
    private String  email;

    public static AccountResponse of(User user){
        return new AccountResponse(user.getId(), user.getEmail());
    }
}
