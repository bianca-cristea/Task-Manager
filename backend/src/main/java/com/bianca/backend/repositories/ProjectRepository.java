package com.bianca.backend.repositories;

import com.bianca.backend.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByProjectName(String name);
}
