package com.example.piyachok.services;

import com.example.piyachok.constants.dao.UserDAO;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private UserDAO userDAO;


    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public UserDTO convertUserToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getEmail(),
                user.getRole(),
                user.isActivated(),
                user.isBlocked(),
                user.getCreationDate(),
                user.getPlaces()
        );
    }

    public ResponseEntity<UserDTO> saveUser(UserDTO userDTO) {
        if (userDTO != null) {
            User user = new User();
            user.setLogin(userDTO.getLogin());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> login(@RequestBody User user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + 5000))
                    .compact();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>("you are loged in", headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<HttpStatus> deleteUserById(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getLogin() != null) {
            userDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return new ResponseEntity<>(userDAO.findAll()
                .stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> findUserByID(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getLogin() != null) {
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserDTO> updateUserById(int id, User user) {
        User oldUser = userDAO.findById(id).orElse(new User());
        if (oldUser.getLogin() != null) {
            user.setId(id);
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
