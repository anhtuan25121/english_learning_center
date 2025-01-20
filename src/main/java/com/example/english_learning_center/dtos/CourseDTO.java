package com.example.english_learning_center.dtos;

import com.example.english_learning_center.models.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String courseName;
    private String description;

    // Constructor nhận đối tượng Course
    public CourseDTO(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.description = course.getDescription();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }






}