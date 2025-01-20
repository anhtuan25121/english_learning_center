package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.StudentLessonDTO;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.IStudentLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentLessonController {

    private final IStudentLessonService lessonService;

    // API tạo buổi học mới
    @PostMapping
    public ResponseEntity<ApiResponse<StudentLessonDTO>> createLesson(@RequestBody StudentLessonDTO lessonDTO) {
        StudentLessonDTO createdLesson = lessonService.createLesson(lessonDTO);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Lesson created successfully.",
                createdLesson
        ));
    }

    // API xóa buổi học
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lesson deleted successfully.",
                null
        ));
    }
}
