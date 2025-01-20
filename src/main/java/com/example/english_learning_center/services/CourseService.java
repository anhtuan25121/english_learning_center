package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.CourseDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.Course;
import com.example.english_learning_center.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(course -> new CourseDTO(
                course.getId(), course.getCourseName(), course.getDescription())).collect(Collectors.toList());
    }

    @Override
    public Optional<CourseDTO> getCourseById(Long id) {
        return courseRepository.findById(id).map(course -> new CourseDTO(
                course.getId(), course.getCourseName(), course.getDescription()));
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());
        Course savedCourse = courseRepository.save(course);
        return new CourseDTO(savedCourse.getId(), savedCourse.getCourseName(), savedCourse.getDescription());
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());
        Course updatedCourse = courseRepository.save(course);
        return new CourseDTO(updatedCourse.getId(), updatedCourse.getCourseName(), updatedCourse.getDescription());
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        courseRepository.delete(course);
        log.info("Deleted course with ID: {}", id);
    }

    @Override
    public Course getCourseByName(String courseName) {
        return courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with name: " + courseName));
    }
}