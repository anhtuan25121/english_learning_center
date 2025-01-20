package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.CourseDTO;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Fetched all courses successfully",
                courses
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(courseDTO -> ResponseEntity.ok(new ApiResponse(
                        HttpStatus.OK.value(), "Course fetched successfully", courseDTO)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                        HttpStatus.NOT_FOUND.value(), "Course not found with id: " + id, null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(
                HttpStatus.CREATED.value(), "Course created successfully", createdCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(), "Course updated successfully", updatedCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(), "Course deleted successfully with id: " + id, null));
    }

    // API để lấy thông tin của một khóa học theo tên
    @GetMapping("/name/{courseName}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseByName(@PathVariable String courseName) {
        CourseDTO courseDTO = new CourseDTO(courseService.getCourseByName(courseName));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Course retrieved successfully", courseDTO));
    }
}

