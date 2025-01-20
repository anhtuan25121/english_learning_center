package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.TeacherSlotDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.Course;
import com.example.english_learning_center.models.Teacher;
import com.example.english_learning_center.models.TeacherSlot;
import com.example.english_learning_center.repositories.CourseRepository;
import com.example.english_learning_center.repositories.TeacherRepository;
import com.example.english_learning_center.repositories.TeacherSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherSlotService implements ITeacherSlotService {

    private final TeacherSlotRepository teacherSlotRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Override
    public void saveTeacherSlots(List<TeacherSlotDTO> teacherSlotDTOs) {
        log.info("Start saving teacher slots...");

        for (TeacherSlotDTO slotDTO : teacherSlotDTOs) {
            log.info("Processing slot: {}", slotDTO);

            // Kiểm tra nếu teacherId null hoặc không hợp lệ
            if (slotDTO.getTeacherId() == null) {
                log.error("Teacher ID is missing in the request.");
                throw new ResourceNotFoundException("Teacher ID is missing.");
            }

            // Tìm giáo viên theo teacherId
            Teacher teacher = teacherRepository.findById(slotDTO.getTeacherId())
                    .orElseThrow(() -> {
                        log.error("Teacher not found with ID: {}", slotDTO.getTeacherId());
                        return new ResourceNotFoundException("Teacher not found with ID: " + slotDTO.getTeacherId());
                    });

            log.info("Found teacher: {}", teacher);

            // Kiểm tra nếu courseId null hoặc không hợp lệ
            if (slotDTO.getCourseId() == null) {
                log.error("Course ID is missing in the request.");
                throw new ResourceNotFoundException("Course ID is missing.");
            }

            // Tìm khóa học theo courseId
            Course course = courseRepository.findById(slotDTO.getCourseId())
                    .orElseThrow(() -> {
                        log.error("Course not found with ID: {}", slotDTO.getCourseId());
                        return new ResourceNotFoundException("Course not found with ID: " + slotDTO.getCourseId());
                    });

            log.info("Found course: {}", course);

            // Kiểm tra dữ liệu ngày và giờ
            if (slotDTO.getAvailabilityDate() == null || slotDTO.getStartTime() == null || slotDTO.getEndTime() == null) {
                log.error("Invalid time slot data: {}", slotDTO);
                throw new IllegalArgumentException("Invalid time slot data.");
            }

            // Tạo đối tượng TeacherSlot từ DTO
            TeacherSlot teacherSlot = new TeacherSlot();
            teacherSlot.setTeacher(teacher);
            teacherSlot.setCourse(course);
            teacherSlot.setAvailabilityDate(slotDTO.getAvailabilityDate());
            teacherSlot.setStartTime(slotDTO.getStartTime());
            teacherSlot.setEndTime(slotDTO.getEndTime());

            log.info("Saving teacher slot: {}", teacherSlot);

            // Lưu vào database
            try {
                teacherSlotRepository.save(teacherSlot);
                log.info("Teacher slot saved successfully: {}", teacherSlot);
            } catch (Exception e) {
                log.error("Error saving teacher slot: {}", e.getMessage());
                throw new RuntimeException("Error saving teacher slot.", e);
            }
        }

        log.info("All teacher slots saved successfully.");
    }

    @Override
    public List<TeacherSlotDTO> getTeacherSlots(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + teacherId));

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

    // Lấy danh sách timeslot khả dụng
    @Override
    public List<TeacherSlotDTO> getAvailableTimeSlots(LocalDate availabilityDate) {
        List<TeacherSlot> teacherSlots = teacherSlotRepository.findAvailableTimeSlotsByDate(availabilityDate);
        return teacherSlots.stream()
                .map(slot -> new TeacherSlotDTO(slot.getId(), slot.getTeacher().getId(), slot.getCourse().getId(),
                        slot.getAvailabilityDate(), slot.getStartTime(), slot.getEndTime()))
                .collect(Collectors.toList());
    }

    // Lấy danh sách giáo viên rảnh trong timeslot cụ thể
    @Override
    public List<String> getAvailableTeachers(LocalDate availabilityDate, String startTime, String endTime) {
        List<Long> teacherIds = teacherSlotRepository.findAvailableTeachersByTimeSlot(availabilityDate, startTime, endTime);
        return teacherIds.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableTeacherNames(LocalDate availabilityDate, LocalTime startTime, LocalTime endTime) {
        if (availabilityDate == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("availabilityDate, startTime, and endTime must not be null");
        }

        try {
            // Định dạng thời gian
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedStartTime = startTime.format(formatter);
            String formattedEndTime = endTime.format(formatter);

            // Gọi repository để lấy danh sách giáo viên
            List<Teacher> teachers = teacherSlotRepository.findAvailableTeachersByDateAndTime(availabilityDate, formattedStartTime, formattedEndTime);

            if (teachers == null || teachers.isEmpty()) {
                log.warn("No teachers found for date: {}, startTime: {}, endTime: {}", availabilityDate, startTime, endTime);
                return Collections.emptyList();
            }

            return teachers.stream()
                    .map(Teacher::getTeacherName) // Lấy tên giáo viên
                    .distinct() // Loại bỏ tên trùng lặp
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error occurred while fetching available teacher names for date: {}, startTime: {}, endTime: {}. Error: {}",
                    availabilityDate, startTime, endTime, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch available teacher names. Please try again.", e);
        }
    }


    @Override
    public List<Long> getAvailableTeacherIds(LocalDate availabilityDate, String startTime, String endTime) {
        // Đảm bảo thời gian đúng định dạng HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedStartTime = LocalTime.parse(startTime, formatter).format(formatter);
        String formattedEndTime = LocalTime.parse(endTime, formatter).format(formatter);

        // Ghi log để debug
        log.info("Finding available teachers for Date: {}, Start Time: {}, End Time: {}",
                availabilityDate, formattedStartTime, formattedEndTime);

        // Gọi Repository để lấy danh sách teacher IDs
        return teacherSlotRepository.findAvailableTeacherIds(availabilityDate, formattedStartTime, formattedEndTime);
    }


    @Override
    public List<String> getAvailableTeacherNamesByCourse(LocalDate availabilityDate, String startTime, String endTime, String courseName) {
        // Kiểm tra tham số đầu vào
        if (availabilityDate == null || startTime == null || endTime == null || courseName == null) {
            throw new IllegalArgumentException("All parameters are required. Please provide availabilityDate, startTime, endTime, and courseName.");
        }

        // Kiểm tra định dạng thời gian (nếu cần)
        if (!isValidTimeFormat(startTime) || !isValidTimeFormat(endTime)) {
            throw new IllegalArgumentException("Invalid time format. Please use HH:mm format for startTime and endTime.");
        }

        try {
            // Gọi repository để lấy danh sách tên giáo viên
            List<String> teacherNames = teacherSlotRepository.findAvailableTeacherNamesByCourse(availabilityDate, startTime, endTime, courseName);

            // Kiểm tra nếu không có giáo viên nào phù hợp
            if (teacherNames == null || teacherNames.isEmpty()) {
                throw new IllegalStateException("No available teachers found for the given parameters.");
            }

            return teacherNames;
        } catch (IllegalArgumentException e) {
            // Lỗi tham số đầu vào
            throw new IllegalArgumentException("Error in input parameters: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            // Lỗi logic (không tìm thấy giáo viên)
            throw new IllegalStateException("No available teachers: " + e.getMessage(), e);
        } catch (Exception e) {
            // Bắt tất cả các lỗi không mong muốn khác
            throw new RuntimeException("An unexpected error occurred while retrieving teacher names: " + e.getMessage(), e);
        }
    }

    // Hàm kiểm tra định dạng thời gian
    private boolean isValidTimeFormat(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(time, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }




}
