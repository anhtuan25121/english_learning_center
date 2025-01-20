package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.TeacherDTO;
import com.example.english_learning_center.dtos.TeacherSlotDTO;
import com.example.english_learning_center.responses.ApiResponse;

import java.util.List;
import java.util.Optional;

public interface ITeacherService {

    void registerTeacher(TeacherDTO teacherDTO);

    void saveAvailableSlots(List<TeacherSlotDTO> slots);

    List<TeacherSlotDTO> getAvailableSlots(String email);

    List<TeacherDTO> getAllTeachers();

    Optional<TeacherDTO> getTeacherById(Long id);

    TeacherDTO createTeacher(TeacherDTO teacherDTO);

    TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO);

    void deleteTeacher(Long id);

    Long getOrCreateTeacherId(TeacherDTO teacherDTO);

    Long getOrCreateTeacherIdByEmail(String email);

    Optional<TeacherDTO> getTeacherByEmail(String email);
}
