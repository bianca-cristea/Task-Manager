package com.bianca.backend.dtos;

import com.bianca.backend.models.AppRole;
import com.bianca.backend.models.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDTO {
    private Long id;
    private AppRole roleName;
}
