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
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user) {
        System.out.println(user);
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById (@PathVariable int id){
        return userService.deleteUserById(id);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById (@PathVariable int id){
        return userService.findUserByID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable int id, @RequestBody User user){
        return userService.updateUserById(id,user);
    }


}
