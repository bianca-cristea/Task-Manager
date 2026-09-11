package com.bianca.backend.dtos;

import com.bianca.backend.models.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDTO {
    private Long taskId;
    private String title;
    private String description;
    private StatusEnum status;
    private Long projectId;
    private Long categoryId;
}
