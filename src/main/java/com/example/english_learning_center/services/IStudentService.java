package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentDTO;
import com.example.english_learning_center.models.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    StudentDTO getOrCreateStudent(StudentDTO studentDTO);

    Student getOrCreateStudentEntity(StudentDTO studentDTO);

    StudentDTO getStudentByEmail(String email);


}
