package com.smartcampus.smartcampus.controller;

import com.smartcampus.smartcampus.dto.LoginRequest;
import com.smartcampus.smartcampus.dto.RegisterRequest;
import com.smartcampus.smartcampus.entity.User;
import com.smartcampus.smartcampus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}