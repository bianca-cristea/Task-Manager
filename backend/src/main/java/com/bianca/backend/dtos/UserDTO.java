package com.bianca.backend.dtos;

import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private List<RoleDTO> roles;
}
