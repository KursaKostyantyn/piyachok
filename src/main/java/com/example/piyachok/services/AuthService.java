package com.example.piyachok.services;

import com.example.piyachok.customExceptions.UserIsNotActivatedException;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.models.RefreshToken;
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
import org.springframework.security.core.context.SecurityContextHolder;
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


    public ResponseEntity<JwtResponseDTO> login(User user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        if (authenticate != null) {
            String jwtToken = jwtUtils.generateTokenFromUsername(authenticate.getName());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getLogin());
            user = userDAO.findUserByLogin(user.getLogin()).orElse(new User());
            if (!user.isActivated()){
                throw new UserIsNotActivatedException(user.getLogin(), "user is not activated");
            }
             return new ResponseEntity<>(new JwtResponseDTO(jwtToken, refreshToken.getToken(), userService.convertUserToUserDTO(user)), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<JwtResponseDTO> refreshToken(RefreshToken refreshTokenRequest) {
         RefreshToken token = refreshTokenService.findRefreshTokenByToken(refreshTokenRequest.getToken());
        if(token.getToken()!=null && refreshTokenService.verifyExpiration(token)){
            User user = token.getUser();
            String newAccessToken = jwtUtils.generateTokenFromUsername(user.getLogin());
            String newRefreshToken = refreshTokenService.createRefreshToken(user.getLogin()).getToken();
            System.out.println("here");
            return new ResponseEntity<>(new JwtResponseDTO(newAccessToken,newRefreshToken,userService.convertUserToUserDTO(user)), HttpStatus.OK);
        }
        throw  new RefreshTokenException(refreshTokenRequest.getToken(), "Refresh token is not in database!");
    }

    public ResponseEntity<UserDTO> getAuthorizedUser(){
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByLogin = userDAO.findUserByLogin(currentUserName).orElse(new User());
        if(userByLogin.getLogin()!=null){
            return new ResponseEntity<>(userService.convertUserToUserDTO(userByLogin),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }




}
