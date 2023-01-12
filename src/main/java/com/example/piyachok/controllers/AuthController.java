package com.example.piyachok.controllers;

import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.JwtResponseDTO;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshToken refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @GetMapping("/getAuthorizedUser")
    public ResponseEntity<UserDTO> getAuthorizedUser() {
        return authService.getAuthorizedUser();
    }

}
