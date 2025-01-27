package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.dtos.Errors.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice()
public class BadRequestControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDetail> handleBindException(BindException exception) {
        String title = exception.getMessage();
        List<String> errors = exception.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorDetail errorDetail = new ErrorDetail(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), title, errors);

        return ResponseEntity.badRequest().body(errorDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetail> handleIllegalArgumentException(IllegalArgumentException exception) {
        String title = exception.getMessage();
        String detail = exception.getMessage();

        ErrorDetail errorDetail = new ErrorDetail(title, HttpStatus.BAD_REQUEST.value(), detail, Collections.singletonList(detail));

        return ResponseEntity.badRequest().body(errorDetail);
    }
}
