package com.example.piyachok.controllers;

import com.example.piyachok.models.RefreshTokenRequest;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.JwtResponseDTO;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return authService.saveUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

}
