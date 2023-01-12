package com.example.piyachok.controllers;

import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }


    @DeleteMapping("users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("users")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int id) {
        return userService.findUserByID(id);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable int id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }



}
