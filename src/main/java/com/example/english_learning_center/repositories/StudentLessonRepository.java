package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentLessonRepository extends JpaRepository<StudentLesson, Long> {
    List<StudentLesson> findByStudentRegistrationId(Long studentRegistrationId);

    void deleteByStudentRegistrationId(Long id);
}