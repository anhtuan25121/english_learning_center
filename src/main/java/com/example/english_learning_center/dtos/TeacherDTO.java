package com.example.english_learning_center.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Long id;
    private String teacherName;
    private String email;
    private String phoneNumber;
    private String zaloPhone;
    private String address;


}