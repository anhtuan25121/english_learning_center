package com.example.english_learning_center.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLessonDTO {
    private Long id;
    private Long studentRegistrationId;
    private Long teacherId; // Đảm bảo trường này tồn tại
    private LocalDate lessonDate;
    private String startTime;
    private String endTime;
    private String lessonStatus;
    private String notes;


}
