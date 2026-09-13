package com.bianca.backend.repositories;

import com.bianca.backend.models.Task;
import com.bianca.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByTitle(String title);


    Page<Task> findByProject_User(User user, Pageable pageRequest);
}
