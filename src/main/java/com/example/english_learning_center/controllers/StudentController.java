package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.StudentDTO;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final IStudentService studentService;

    // API tạo mới hoặc cập nhật học sinh
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<StudentDTO>> createOrUpdateStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO result = studentService.getOrCreateStudent(studentDTO);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Student created or updated successfully.",
                result
        ));
    }

    // API lấy thông tin học sinh theo email
    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByEmail(@PathVariable String email) {
        StudentDTO student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Student retrieved successfully.",
                student
        ));
    }
}
