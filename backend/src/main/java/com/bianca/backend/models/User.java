package com.bianca.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min=5,max=30,message = "Username should have the length between 5 and 50.")
    private String username;

    @NotEmpty(message="Email should not be empty.")
    @Email(message = "Email should be valid.")
    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;


}
