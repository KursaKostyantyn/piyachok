package com.example.piyachok.services;

import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private UserDAO userDAO;


    private PasswordEncoder passwordEncoder;

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
                user.getPlaces(),
                user.getNews()
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
