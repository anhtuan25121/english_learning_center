package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {
    List<StudentRegistration> findByStudentId(Long studentId);
    List<StudentRegistration> findByCourseId(Long courseId);

    Optional<StudentRegistration> findByStudentAndCourse(Student student, Course course);

}