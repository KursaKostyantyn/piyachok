package com.example.piyachok.customExceptions;

public class UserIsNotActivatedException extends RuntimeException{

    public UserIsNotActivatedException(String userLogin, String message){
        super(String.format("Failed for [%s]: %s",userLogin,message));
    }
}
