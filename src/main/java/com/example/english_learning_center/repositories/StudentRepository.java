package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
//    Optional<Student> findByEmailAndPhoneNumber(String email, String phoneNumber);

    Optional<Student> findByEmail(String email);
}