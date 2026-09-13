package com.bianca.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
