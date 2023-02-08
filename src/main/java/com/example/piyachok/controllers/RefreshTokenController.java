package com.example.piyachok.controllers;

import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class RefreshTokenController {
    private RefreshTokenService refreshTokenService;
    @GetMapping("/refreshTokens")
    public ResponseEntity<List<RefreshToken>> getAllRefreshTokens(){
        return refreshTokenService.getAllRefreshTokens();
    }
}
