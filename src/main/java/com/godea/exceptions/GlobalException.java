package com.godea.exceptions;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

// @RestControllerAdvice - позволяет отлавливать и контроллировать все ошибки
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userException(UserException exp, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(exp.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> messageException(MessageException messExp, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(messExp.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> methodException(MethodArgumentNotValidException methodExp, WebRequest req) {
        String error = methodExp.getBindingResult().getFieldError().getDefaultMessage();

        ErrorDetail errorDetail = new ErrorDetail("Validation Error", error, LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> handlerException(NoHandlerFoundException handerExp, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail("Endpoint not found", handerExp.getMessage(), LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> otherException(Exception allExp, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(allExp.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
