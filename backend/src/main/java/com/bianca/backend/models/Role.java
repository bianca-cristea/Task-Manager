package com.bianca.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppRole roleName;



    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
