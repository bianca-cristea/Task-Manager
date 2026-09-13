package com.bianca.backend.repositories;

import com.bianca.backend.models.Project;
import com.bianca.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByProjectName(String name);

    Page<Project> findByUser(User user, Pageable pageRequest);
}
