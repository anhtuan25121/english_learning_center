package com.example.english_learning_center.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String studentName;
    private String email;
    private String phoneNumber;
    private String kakaotalkPhone;
}
