package com.example.piyachok.controllers;

import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.models.dto.CustomErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<CustomErrorDTO> refreshTokenError(RefreshTokenException e) {
        return new ResponseEntity<>(new CustomErrorDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
