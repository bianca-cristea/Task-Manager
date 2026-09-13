package com.bianca.backend.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min=5, message = "Username should have at least 5 letters.")
    private String username;

    @NotBlank
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 5, message = "Password should have at least 5 characters.")
    private String password;
}
