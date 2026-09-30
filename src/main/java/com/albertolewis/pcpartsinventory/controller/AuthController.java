package com.albertolewis.pcpartsinventory.controller;

import com.albertolewis.pcpartsinventory.dto.LoginRequest;
import com.albertolewis.pcpartsinventory.dto.RegisterRequest;
import com.albertolewis.pcpartsinventory.dto.UserResponse;
import com.albertolewis.pcpartsinventory.model.User;
import com.albertolewis.pcpartsinventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        UserResponse response = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.loginUser(loginRequest);
        UserResponse response = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(response);
    }


}
