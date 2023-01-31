package com.example.piyachok.controllers;

import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("")
public class UserController {
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }


    @DeleteMapping("users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("users/")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int id) {
        return userService.findUserByID(id);
    }

    @PutMapping("users/update")
    public ResponseEntity<UserDTO> updateUserById(@RequestParam int id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }

    @GetMapping("users/favoritePlaces")
    public ResponseEntity<ItemListDTO<PlaceDTO>> getFavoritePlacesByUserLogin(@RequestParam String login, @RequestParam(required = false) Integer page) {
        return userService.getFavoritePlacesByUserLogin(login, page);
    }

    @PutMapping("users/favoritePlaces/add")
    public ResponseEntity<UserDTO> addPlaceToFavoriteByPlaceIdAndUserLogin(@RequestParam int placeId, @RequestParam String login) {
        return userService.addPlaceToFavoriteByPlaceIdAndUserLogin(placeId, login);
    }

    @GetMapping("users/favoritePlaces/check")
    public ResponseEntity<Boolean> checkPlaceIsFavoriteByPlaceIdAndUserLogin(@RequestParam int placeId, @RequestParam String login) {
        return userService.checkPlaceIsFavoriteByPlaceIdAndUserLogin(placeId, login);
    }

    @DeleteMapping("users/favoritePlaces/delete")
    public ResponseEntity<UserDTO> deletePlaceFromFavoriteByPlaceIdUserLogin(@RequestParam int placeId,@RequestParam String login){
        return userService.deletePlaceFromFavoriteByPlaceIdUserLogin(placeId,login);
    }

    @GetMapping("activate")
    public ResponseEntity<UserDTO> activateUserById (@RequestParam int userId){
        return userService.activateUserById(userId);
    }

}
