package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentLessonDTO;
import com.example.english_learning_center.dtos.StudentRegistrationDTO;
import com.example.english_learning_center.models.StudentLesson;
import com.example.english_learning_center.models.StudentRegistration;

import java.util.List;

public interface IStudentRegistrationService {

    // Tạo mới hoặc cập nhật đăng ký của học sinh
    StudentRegistrationDTO createOrUpdateRegistration(StudentRegistrationDTO registrationDTO);


    // Hàm tạo StudentLesson từ DTO

    StudentLesson createStudentLesson(StudentLessonDTO lessonDTO, StudentRegistration registration);

    StudentRegistrationDTO getRegistrationById(Long id);
}
