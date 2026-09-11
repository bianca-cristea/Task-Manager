package com.bianca.backend.dtos;

import com.bianca.backend.models.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String description;
    private Long userId;
}
