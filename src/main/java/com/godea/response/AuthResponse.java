package com.godea.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

@Getter
@Setter
@Scope("request")
public class AuthResponse {
    private String jwt;
    private boolean isAuth;
}
