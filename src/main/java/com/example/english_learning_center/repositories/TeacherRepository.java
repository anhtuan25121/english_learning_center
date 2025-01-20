package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findById(Long id);

}
