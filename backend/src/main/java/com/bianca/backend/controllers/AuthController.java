package com.bianca.backend.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.bianca.backend.dtos.AuthenticationResult;
import com.bianca.backend.security.request.LoginRequest;
import com.bianca.backend.security.request.SignupRequest;
import com.bianca.backend.security.response.MessageResponse;
import com.bianca.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                result.getJwtCookie().toString()).body(result.getResponse());
    }

    @Operation(summary = "Register user")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.register(signupRequest);
    }

    @Operation(summary = "Get username")
    @GetMapping("/username")
    public String getUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else
            return "";
    }
    @Operation(summary = "Get user details")
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No auth in context");
        }
        return ResponseEntity.ok(authService.getUserDetails(authentication));
    }

    @Operation(summary = "Sign out user")
    @PostMapping("/signout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = authService.logout();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out."));
    }



}

