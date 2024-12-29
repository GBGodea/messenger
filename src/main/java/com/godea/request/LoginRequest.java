package com.godea.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

@Getter
@Setter
@Scope("request")
public class LoginRequest {
    private String email;
    private String password;
}
