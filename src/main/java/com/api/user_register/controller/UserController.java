package com.api.user_register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.user_register.dto.UserRequestDTO;
import com.api.user_register.dto.UserResponseDTO;
import com.api.user_register.model.User;
import com.api.user_register.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
