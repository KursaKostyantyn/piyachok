package com.example.piyachok.customExceptions;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String token, String message){
        super(String.format("Failed for [%s]: %s",token,message));
    }
}
