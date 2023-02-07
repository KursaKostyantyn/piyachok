package com.example.piyachok.controllers;

import com.example.piyachok.customExceptions.JwtTokenException;
import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.customExceptions.UserIsNotActivatedException;
import com.example.piyachok.models.dto.CustomErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<CustomErrorDTO> refreshTokenError(RefreshTokenException e) {
        return new ResponseEntity<>(new CustomErrorDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomErrorDTO> loginExistError(SQLIntegrityConstraintViolationException ex) {
        return new ResponseEntity<>(new CustomErrorDTO("Login already exists"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserIsNotActivatedException.class)
    public ResponseEntity<CustomErrorDTO> userIsNotActivatedException(UserIsNotActivatedException e){
        return new ResponseEntity<>(new CustomErrorDTO(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = JwtTokenException.class)
    public ResponseEntity<CustomErrorDTO> jwtTokenException(JwtTokenException e){
        return new ResponseEntity<>(new CustomErrorDTO(e.getMessage()),HttpStatus.BAD_REQUEST);
    }



}
