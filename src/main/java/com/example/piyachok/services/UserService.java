package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.models.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private UserDAO userDAO;
    private PlaceDAO placeDAO;

    private ItemListService itemListService;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;


    public static UserDTO convertUserToUserDTO(User user) {
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
            user.setEmail(userDTO.getEmail());
            userDAO.save(user);
            if (user.getId() != 0) {
                mailService.sendMail(user);
            }
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
                .map(UserService::convertUserToUserDTO)
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
            news.setUser(user);
            return true;
        }
        return false;
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> getFavoritePlacesByUserLogin(String login, Integer page) {
        boolean old = false;
        int itemsOnPage = 10;
        User user = userDAO.findUserByLogin(login).orElse(new User());

        if (user.getLogin() != null) {
            List<PlaceDTO> placeDTOS = user.getFavoritePlaces()
                    .stream()
                    .map(PlaceService::convertPlaceToPlaceDTO).collect(Collectors.toList());
            return itemListService.createResponseEntity(placeDTOS, itemsOnPage, page, old);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<UserDTO> addPlaceToFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getFavoritePlaces()
                .stream()
                .anyMatch(favoritePlace -> favoritePlace == place)) {
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        if (user.getLogin() != null && place.getName() != null) {
            user.getFavoritePlaces().add(place);
            userDAO.save(user);


            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> checkPlaceIsFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getLogin() == null || place.getName() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.getFavoritePlaces()
                .stream()
                .anyMatch(favoritePlace -> favoritePlace == place)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> deletePlaceFromFavoriteByPlaceIdUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getLogin() == null || place.getName() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getFavoritePlaces().remove(place)) {
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserDTO> activateUserById(int userId) {
        System.out.println("userId"+userId);
        User user = userDAO.findById(userId).orElse(new User());
        if (user.getLogin() != null) {
            user.setActivated(true);
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
