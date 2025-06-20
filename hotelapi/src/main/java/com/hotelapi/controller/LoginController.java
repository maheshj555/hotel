package com.hotelapi.controller;

import com.hotelapi.dto.LoginForm;
import com.hotelapi.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Operation(summary = "Login with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm) {
        if ("test@example.com".equalsIgnoreCase(loginForm.getEmail()) &&
            "password".equals(loginForm.getPassword())) {

            String accessToken = "token";
            String refreshToken = "token";

            return ResponseEntity.ok(new LoginResponse(
                    true, "Login successful", accessToken, refreshToken
            ));
        }

        return ResponseEntity.status(401).body(new LoginResponse(
                false, "Invalid email or password", null, null
        ));
    }
}
