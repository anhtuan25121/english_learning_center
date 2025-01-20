package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.CourseDTO;
import com.example.english_learning_center.models.Course;

import java.util.List;
import java.util.Optional;

public interface ICourseService {
    List<CourseDTO> getAllCourses();
    Optional<CourseDTO> getCourseById(Long id);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);

    Course getCourseByName(String courseName);
}
