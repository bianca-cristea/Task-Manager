package com.bianca.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @NotEmpty(message = "Title should not be empty.")
    private String title;

    @NotEmpty(message = "Description should not be empty.")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Project project;

}
