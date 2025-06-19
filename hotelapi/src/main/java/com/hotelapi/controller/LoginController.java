package com.hotelapi.controller;

import com.hotelapi.dto.LoginForm;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RefreshTokenRequest;
import com.hotelapi.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // for React use ur port
public class LoginController {

    private final JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginForm loginForm) {

        if ("test@example.com".equalsIgnoreCase(loginForm.getEmail()) &&
            "password".equals(loginForm.getPassword())) {

            String accessToken = jwtService.generateAccessToken(loginForm.getEmail());
            String refreshToken = jwtService.generateRefreshToken(loginForm.getEmail());

            return ResponseEntity.ok(new LoginResponse(true, "Login successful", accessToken, refreshToken));
        }

        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid credentials", null, null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(403).body(new LoginResponse(false, "Invalid refresh token", null, null));
        }

        String email = jwtService.extractEmail(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(email);

        return ResponseEntity.ok(new LoginResponse(true, "Token refreshed", newAccessToken, refreshToken));
    }
}
