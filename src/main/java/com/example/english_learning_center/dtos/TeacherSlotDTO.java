package com.example.english_learning_center.dtos;

import com.example.english_learning_center.models.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSlotDTO {
    private Long id;
    private Long teacherId;
    private Long courseId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate availabilityDate;

    private String startTime;
    private String endTime;

    // Constructor
    public TeacherSlotDTO(LocalDate availabilityDate, String startTime, String endTime) {
        this.availabilityDate = availabilityDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
