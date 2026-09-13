package com.bianca.backend.services;

import com.bianca.backend.dtos.AuthenticationResult;
import com.bianca.backend.security.request.LoginRequest;
import com.bianca.backend.security.request.SignupRequest;
import com.bianca.backend.security.response.MessageResponse;
import com.bianca.backend.security.response.UserInfoResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public AuthenticationResult login(LoginRequest loginRequest);
    public ResponseEntity<MessageResponse> register(SignupRequest signupRequest);
    public UserInfoResponse getUserDetails(Authentication authentication);
    public ResponseCookie logout();
}
