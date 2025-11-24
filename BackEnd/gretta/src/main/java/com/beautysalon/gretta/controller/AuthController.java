package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.auth.LoginRequest;
import com.beautysalon.gretta.dto.auth.LoginResponse;
import com.beautysalon.gretta.dto.auth.RegisterRequest;
import com.beautysalon.gretta.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("Login attempt for: " + request.getCorreo());
        LoginResponse response = authService.login(request);
        System.out.println("Login successful for: " + request.getCorreo());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("Register attempt for: " + request.getCorreo());
        String mensaje = authService.register(request);
        System.out.println("Register successful for: " + request.getCorreo());
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
