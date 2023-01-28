package com.example.piyachok.services;

import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.Place;
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
            System.out.println("user = " + user);
            if (!user.getPassword().equals("")) {
                System.out.println("user.getPassword() = " + user.getPassword());
                oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setBirthDate(user.getBirthDate());
            oldUser.setEmail(user.getEmail());
            oldUser.setRole(user.getRole());
            oldUser.setActivated(user.isActivated());
            oldUser.setBlocked(user.isBlocked());


            userDAO.save(oldUser);
            return new ResponseEntity<>(convertUserToUserDTO(oldUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public boolean addNewsToUser(String login, News news) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin() != null) {
            user.getNews().add(news);
            //todo delete?
//            userDAO.save(user);
            news.setUser(user);
            return true;
        }
        return false;
    }




}
