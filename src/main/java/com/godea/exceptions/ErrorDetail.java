package com.godea.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorDetail {
    private String error;
    private String message;
    private LocalDateTime errorTime;
}
