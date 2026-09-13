package com.bianca.backend.dtos;

import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private List<RoleDTO> roles;
}
