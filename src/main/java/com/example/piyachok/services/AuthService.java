package com.example.piyachok.services;

import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.models.RefreshTokenRequest;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.JwtResponseDTO;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private UserDAO userDAO;
    private RefreshTokenService refreshTokenService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    public ResponseEntity<UserDTO> saveUser(UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    public ResponseEntity<JwtResponseDTO> login(User user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        if (authenticate != null) {
            String jwtToken = jwtUtils.generateTokenFromUsername(authenticate.getName());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            System.out.println("user.getLogin() = " + user.getLogin());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getLogin());
            return new ResponseEntity<>(new JwtResponseDTO(jwtToken, refreshToken.getToken(), user), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<JwtResponseDTO> refreshToken(RefreshTokenRequest request) {
        String refreshTokenRequest = request.getRefreshToken();
        return refreshTokenService.findRefreshTokenByToken(refreshTokenRequest)
                .map(token -> refreshTokenService.verifyExpiration(token))
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getLogin());
                    String refreshToken = refreshTokenService.createRefreshToken(user.getLogin()).getToken();
                    return new ResponseEntity<>(new JwtResponseDTO(token,refreshToken,user),HttpStatus.OK);
                })
                .orElseThrow(() -> new RefreshTokenException(refreshTokenRequest, "Refresh token is not in database!"));
    }


}
