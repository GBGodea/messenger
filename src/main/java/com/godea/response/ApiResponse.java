package com.godea.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

@Getter
@Setter
// Так как есть состояние, наподобии status, то scope = request
@Scope("request")
public class ApiResponse {
    private String message;
    private boolean status;
}
