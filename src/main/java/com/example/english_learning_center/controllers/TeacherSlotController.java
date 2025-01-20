package com.example.english_learning_center.controllers;

import com.example.english_learning_center.dtos.TeacherDTO;
import com.example.english_learning_center.dtos.TeacherSlotDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.responses.ApiResponse;
import com.example.english_learning_center.services.ITeacherSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/teacher-slots")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class TeacherSlotController {

    private final ITeacherSlotService teacherSlotService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveTeacherSlots(@Valid @RequestBody List<TeacherSlotDTO> teacherSlotDTOs) {
        log.info("Received request to save teacher slots: {}", teacherSlotDTOs);

        try {
            teacherSlotService.saveTeacherSlots(teacherSlotDTOs);
            log.info("Teacher slots saved successfully.");
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK.value(),
                    "Teacher slots saved successfully",
                    null
            ));
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Error: " + e.getMessage(),
                    null
            ));
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error: " + e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error saving teacher slots: " + e.getMessage(),
                    null
            ));
        }
    }



    @GetMapping("/{teacherId}")
    public ResponseEntity<ApiResponse> getTeacherSlots(@PathVariable Long teacherId) {
        List<TeacherSlotDTO> slots = teacherSlotService.getTeacherSlots(teacherId);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Fetched teacher slots successfully",
                slots
        ));
    }

    // API lấy danh sách timeslot khả dụng trong ngày
    @GetMapping("/available-times")
    public ResponseEntity<List<TeacherSlotDTO>> getAvailableTimeSlots(@RequestParam String availabilityDate) {
        LocalDate date = LocalDate.parse(availabilityDate);
        List<TeacherSlotDTO> availableTimeSlots = teacherSlotService.getAvailableTimeSlots(date);
        return ResponseEntity.ok(availableTimeSlots);
    }

//    // API lấy danh sách giáo viên rảnh trong timeslot cụ thể
//    @GetMapping("/available-teachers")
//    public ResponseEntity<?> getAvailableTeachers(
//            @RequestParam("availabilityDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availabilityDate,
//            @RequestParam("startTime") String startTime,
//            @RequestParam("endTime") String endTime) {
//
//        List<TeacherDTO> availableTeachers = teacherSlotService.getAvailableTeachersWithNames(availabilityDate, startTime, endTime);
//
//        if (availableTeachers.isEmpty()) {
//            log.warn("No teachers available for Date: {}, Start Time: {}, End Time: {}", availabilityDate, startTime, endTime);
//            return ResponseEntity.ok(Collections.emptyList());
//        }
//
//        return ResponseEntity.ok(availableTeachers);
//    }

    @GetMapping("/available-teacher-names")
    public ResponseEntity<?> getAvailableTeacherNamesByCourse(
            @RequestParam("availabilityDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availabilityDate,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("courseName") String courseName) {

        try {
            // Gọi service để lấy danh sách tên giáo viên
            List<String> teacherNames = teacherSlotService.getAvailableTeacherNamesByCourse(availabilityDate, startTime, endTime, courseName);

            // Trả về kết quả danh sách tên giáo viên
            return ResponseEntity.ok(teacherNames);

        } catch (IllegalArgumentException e) {
            // Xử lý lỗi tham số không hợp lệ
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Xử lý logic khi không tìm thấy giáo viên
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }


}