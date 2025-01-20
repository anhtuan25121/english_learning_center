package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.TeacherDTO;
import com.example.english_learning_center.dtos.TeacherSlotDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITeacherSlotService {
    void saveTeacherSlots(List<TeacherSlotDTO> teacherSlotDTOs);

    List<TeacherSlotDTO> getTeacherSlots(Long teacherId);

    // Lấy danh sách timeslot khả dụng
    List<TeacherSlotDTO> getAvailableTimeSlots(LocalDate availabilityDate);

    // Lấy danh sách giáo viên rảnh trong timeslot cụ thể
    List<String> getAvailableTeachers(LocalDate availabilityDate, String startTime, String endTime);


    List<String> getAvailableTeacherNames(LocalDate availabilityDate, LocalTime startTime, LocalTime endTime);

    List<Long> getAvailableTeacherIds(LocalDate availabilityDate, String startTime, String endTime);


    List<String> getAvailableTeacherNamesByCourse(LocalDate availabilityDate, String startTime, String endTime, String courseName);
}
