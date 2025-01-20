package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.courseName = :courseName")
    Optional<Course> findByCourseName(@Param("courseName") String courseName);


}