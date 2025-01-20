package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.TeacherDTO;
import com.example.english_learning_center.dtos.TeacherSlotDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.ITeacherService;
import com.example.english_learning_center.services.ITeacherSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class TeacherController {

    private final ITeacherService teacherService;
    private final ITeacherSlotService teacherSlotService;

    // ✅ 1. Get all teachers
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Fetched all teachers successfully",
                teachers
        ));
    }

    // ✅ 2. Get teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
                .map(teacherDTO -> ResponseEntity.ok(
                        new ApiResponse(HttpStatus.OK.value(), "Teacher fetched successfully", teacherDTO)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(HttpStatus.NOT_FOUND.value(),
                                "Teacher not found with ID: " + id, null)));
    }

    // ✅ 3. Create a new teacher
    @PostMapping
    public ResponseEntity<ApiResponse> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(
                HttpStatus.CREATED.value(),
                "Teacher created successfully",
                createdTeacher
        ));
    }

    // ✅ 4. Update an existing teacher
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Teacher updated successfully",
                updatedTeacher
        ));
    }

    // ✅ 5. Delete a teacher by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Teacher deleted successfully with ID: " + id,
                null
        ));
    }

    // ✅ 6. Save available slots for a teacher
    @PostMapping("/slots")
    public ResponseEntity<ApiResponse> saveAvailableSlots(@Valid @RequestBody List<TeacherSlotDTO> teacherSlotDTOs) {
        teacherSlotService.saveTeacherSlots(teacherSlotDTOs);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Teacher slots saved successfully",
                null
        ));
    }

    // ✅ 7. Get available slots for a teacher by email
    @GetMapping("/slots/{email}")
    public ResponseEntity<ApiResponse> getAvailableSlots(@PathVariable String email) {
        List<TeacherSlotDTO> slots = teacherService.getAvailableSlots(email);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Fetched available slots successfully",
                slots
        ));
    }

    // ✅ 8. Tìm thông tin giáo viên và lịch rảnh theo email
    @GetMapping("/registered-classes/{email}")
    public ResponseEntity<ApiResponse> searchTeacherWithSlots(@PathVariable String email) {
        // 1. Tìm thông tin giáo viên qua email
        TeacherDTO teacherDTO = teacherService.getTeacherByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + email));

        // 2. Lấy danh sách timeslot lịch rảnh của giáo viên
        List<TeacherSlotDTO> availableSlots = teacherService.getAvailableSlots(email);

        // 3. Tạo phản hồi API gồm thông tin giáo viên và lịch rảnh
        Map<String, Object> response = new HashMap<>();
        response.put("teacherInfo", teacherDTO);
        response.put("availableSlots", availableSlots);

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Fetched teacher info and available slots successfully",
                response
        ));
    }


    // ✅ 9. Register teacher (create or update)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        teacherService.registerTeacher(teacherDTO);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Teacher registered or updated successfully",
                null
        ));
    }

    // ✅ Endpoint để tìm giáo viên qua email
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchTeacherByEmail(@RequestParam String email) {
        Optional<TeacherDTO> teacherDTO = teacherService.getTeacherByEmail(email);
        if (teacherDTO.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK.value(),
                    "Teacher found successfully",
                    teacherDTO.get()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Teacher not found with email: " + email,
                    null
            ));
        }
    }


}
