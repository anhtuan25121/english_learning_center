package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.StudentRegistrationDTO;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.IStudentRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-registrations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentRegistrationController {

    private final IStudentRegistrationService registrationService;

    // API tạo mới hoặc cập nhật đăng ký
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<StudentRegistrationDTO>> createOrUpdateRegistration(
            @RequestBody StudentRegistrationDTO registrationDTO) {
        StudentRegistrationDTO result = registrationService.createOrUpdateRegistration(registrationDTO);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Student registration created or updated successfully.",
                result
        ));
    }

    // API lấy thông tin đăng ký
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentRegistrationDTO>> getRegistrationById(@PathVariable Long id) {
        StudentRegistrationDTO registration = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Registration retrieved successfully.",
                registration
        ));
    }
}
