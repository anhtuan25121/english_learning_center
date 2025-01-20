package com.example.english_learning_center.services;


import com.example.english_learning_center.dtos.TeacherDTO;
import com.example.english_learning_center.dtos.TeacherSlotDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.Teacher;
import com.example.english_learning_center.models.TeacherSlot;
import com.example.english_learning_center.repositories.TeacherRepository;
import com.example.english_learning_center.repositories.TeacherSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherSlotRepository teacherSlotRepository;

    @Override
    public void registerTeacher(TeacherDTO teacherDTO) {
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(teacherDTO.getEmail());

        Teacher teacher = existingTeacher.orElseGet(Teacher::new);
        teacher.setTeacherName(teacherDTO.getTeacherName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        teacher.setZaloPhone(teacherDTO.getZaloPhone());
        teacher.setAddress(teacherDTO.getAddress());

        teacherRepository.save(teacher);
        log.info("Teacher registered or updated: {}", teacherDTO.getEmail());
    }

    @Override
    public void saveAvailableSlots(List<TeacherSlotDTO> slots) {
        List<TeacherSlot> teacherSlots = slots.stream().map(slot -> {
            // Tìm giáo viên theo teacherId từ TeacherSlotDTO
            Teacher teacher = teacherRepository.findById(slot.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + slot.getTeacherId()));

            TeacherSlot teacherSlot = new TeacherSlot();
            teacherSlot.setTeacher(teacher);
            teacherSlot.setAvailabilityDate(slot.getAvailabilityDate());
            teacherSlot.setStartTime(slot.getStartTime());
            teacherSlot.setEndTime(slot.getEndTime());
            return teacherSlot;
        }).collect(Collectors.toList());

        teacherSlotRepository.saveAll(teacherSlots);
        log.info("Saved available slots for teachers.");
    }

    @Override
    public List<TeacherSlotDTO> getAvailableSlots(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + email));

        List<TeacherSlot> slots = teacherSlotRepository.findByTeacher(teacher);
        return slots.stream().map(slot -> new TeacherSlotDTO(
                slot.getId(),
                teacher.getId(),
                slot.getCourse().getId(),
                slot.getAvailabilityDate(),
                slot.getStartTime(),
                slot.getEndTime()
        )).collect(Collectors.toList());
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacher -> new TeacherDTO(
                        teacher.getId(),
                        teacher.getTeacherName(),
                        teacher.getEmail(),
                        teacher.getPhoneNumber(),
                        teacher.getZaloPhone(),
                        teacher.getAddress()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<TeacherDTO> getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(teacher -> new TeacherDTO(
                        teacher.getId(),
                        teacher.getTeacherName(),
                        teacher.getEmail(),
                        teacher.getPhoneNumber(),
                        teacher.getZaloPhone(),
                        teacher.getAddress()
                ));
    }


    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setTeacherName(teacherDTO.getTeacherName());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        teacher.setZaloPhone(teacherDTO.getZaloPhone());
        teacher.setAddress(teacherDTO.getAddress());

        Teacher savedTeacher = teacherRepository.save(teacher);
        return new TeacherDTO(
                savedTeacher.getId(),
                savedTeacher.getTeacherName(),
                savedTeacher.getEmail(),
                savedTeacher.getPhoneNumber(),
                savedTeacher.getZaloPhone(),
                savedTeacher.getAddress()
        );
    }


    @Override
    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        existingTeacher.setTeacherName(teacherDTO.getTeacherName());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        existingTeacher.setZaloPhone(teacherDTO.getZaloPhone());
        existingTeacher.setAddress(teacherDTO.getAddress());

        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return new TeacherDTO(
                updatedTeacher.getId(),
                updatedTeacher.getTeacherName(),
                updatedTeacher.getEmail(),
                updatedTeacher.getPhoneNumber(),
                updatedTeacher.getZaloPhone(),
                updatedTeacher.getAddress()
        );
    }


    @Override
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        teacherRepository.delete(teacher);
        log.info("Deleted teacher with id: {}", id);
    }

    @Override
    public Long getOrCreateTeacherId(TeacherDTO teacherDTO) {
        // Kiểm tra giáo viên qua email
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(teacherDTO.getEmail());

        if (existingTeacher.isPresent()) {
            // Nếu giáo viên đã tồn tại, cập nhật thông tin
            Teacher teacher = existingTeacher.get();
            teacher.setTeacherName(teacherDTO.getTeacherName());
            teacher.setPhoneNumber(teacherDTO.getPhoneNumber());
            teacher.setZaloPhone(teacherDTO.getZaloPhone());
            teacher.setAddress(teacherDTO.getAddress());

            // Lưu thông tin đã cập nhật
            teacherRepository.save(teacher);
            log.info("Updated existing teacher with email: {}", teacherDTO.getEmail());
            return teacher.getId();
        } else {
            // Nếu giáo viên chưa tồn tại, tạo mới giáo viên
            Teacher newTeacher = new Teacher();
            newTeacher.setTeacherName(teacherDTO.getTeacherName());
            newTeacher.setEmail(teacherDTO.getEmail());
            newTeacher.setPhoneNumber(teacherDTO.getPhoneNumber());
            newTeacher.setZaloPhone(teacherDTO.getZaloPhone());
            newTeacher.setAddress(teacherDTO.getAddress());

            Teacher savedTeacher = teacherRepository.save(newTeacher);
            log.info("Created new teacher with email: {}", teacherDTO.getEmail());
            return savedTeacher.getId();
        }
    }

    @Override
    public Long getOrCreateTeacherIdByEmail(String email) {
        // Kiểm tra giáo viên qua email
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(email);

        if (existingTeacher.isPresent()) {
            return existingTeacher.get().getId();
        } else {
            throw new ResourceNotFoundException("Teacher not found with email: " + email);
        }
    }

    @Override
    public Optional<TeacherDTO> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .map(teacher -> new TeacherDTO(
                        teacher.getId(),
                        teacher.getTeacherName(),
                        teacher.getEmail(),
                        teacher.getPhoneNumber(),
                        teacher.getZaloPhone(),
                        teacher.getAddress()
                ));
    }

}
