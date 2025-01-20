package com.example.english_learning_center.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationDTO {
    private Long id;
    private StudentDTO student;
    private String courseName;
    private String status;
    private List<StudentLessonDTO> lessons;


}
