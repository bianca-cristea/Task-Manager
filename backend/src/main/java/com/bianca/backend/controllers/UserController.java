package com.bianca.backend.controllers;

import com.bianca.backend.dtos.UserDTO;
import com.bianca.backend.dtos.UserRegisterDTO;
import com.bianca.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        UserDTO userDTO = userService.getUserById(id);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @RequestBody UserRegisterDTO userRegisterDTO
    ) {

        UserDTO createdUser = userService.createUser(userRegisterDTO);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser( @PathVariable Long id, @RequestBody UserRegisterDTO userRegisterDTO
    ) {
        UserDTO updatedUser = userService.updateUser(id, userRegisterDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<UserDTO> deleteUser(Long id) {

        UserDTO deletedUser = userService.deleteUser(id);

        return ResponseEntity.ok(deletedUser);
    }

}
