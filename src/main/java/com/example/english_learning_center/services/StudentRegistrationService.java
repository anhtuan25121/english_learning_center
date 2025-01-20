package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentLessonDTO;
import com.example.english_learning_center.dtos.StudentRegistrationDTO;
import com.example.english_learning_center.dtos.StudentDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.*;
import com.example.english_learning_center.repositories.StudentLessonRepository;
import com.example.english_learning_center.repositories.StudentRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentRegistrationService implements IStudentRegistrationService {

    private final StudentRegistrationRepository registrationRepository;
    private final StudentLessonRepository lessonRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TeacherService teacherService;


    @Override
    @Transactional
    public StudentRegistrationDTO createOrUpdateRegistration(StudentRegistrationDTO registrationDTO) {
        // Xử lý hoặc tạo Student từ DTO
        Student student = studentService.getOrCreateStudentEntity(registrationDTO.getStudent());

        // Kiểm tra hoặc lấy Course từ DTO
        Course course = courseService.getCourseByName(registrationDTO.getCourseName());

        // Tìm hoặc tạo mới StudentRegistration
        StudentRegistration existingRegistration = registrationRepository.findByStudentAndCourse(student, course).orElse(null);
        StudentRegistration registration = existingRegistration != null
                ? existingRegistration
                : new StudentRegistration(student, course, StudentRegistration.RegistrationStatus.pending);

        // Reset danh sách buổi học nếu đã tồn tại
        if (existingRegistration != null) {
            // Xóa các bài học cũ trước khi thêm mới
            registration.getStudentLessons().clear();
        } else {
            registration.setStudentLessons(new ArrayList<>());
        }

        // Tạo danh sách bài học mới từ DTO
        StudentRegistration finalRegistration = registration;
        List<StudentLesson> lessons = registrationDTO.getLessons().stream()
                .map(lessonDTO -> createStudentLesson(lessonDTO, finalRegistration))
                .toList();

        // Thêm danh sách bài học mới
        registration.getStudentLessons().addAll(lessons);

        // Lưu bản ghi StudentRegistration
        registration = registrationRepository.save(registration);

        return convertToDTO(registration);
    }



    @Override
    public StudentLesson createStudentLesson(StudentLessonDTO lessonDTO, StudentRegistration registration) {
        StudentLesson lesson = new StudentLesson();
        lesson.setStudentRegistration(registration);
        lesson.setLessonDate(lessonDTO.getLessonDate());
        lesson.setStartTime(lessonDTO.getStartTime());
        lesson.setEndTime(lessonDTO.getEndTime());
        lesson.setLessonStatus(StudentLesson.LessonStatus.valueOf(lessonDTO.getLessonStatus()));
        lesson.setNotes(lessonDTO.getNotes());

        // Lấy Teacher từ teacherId và ánh xạ TeacherDTO thành Teacher
        if (lessonDTO.getTeacherId() != null) {
            Teacher teacher = teacherService.getTeacherById(lessonDTO.getTeacherId())
                    .map(dto -> {
                        Teacher mappedTeacher = new Teacher();
                        mappedTeacher.setId(dto.getId());
                        mappedTeacher.setTeacherName(dto.getTeacherName());
                        mappedTeacher.setEmail(dto.getEmail());
                        mappedTeacher.setPhoneNumber(dto.getPhoneNumber());
                        mappedTeacher.setZaloPhone(dto.getZaloPhone());
                        mappedTeacher.setAddress(dto.getAddress());
                        return mappedTeacher;
                    })
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + lessonDTO.getTeacherId()));
            lesson.setTeacher(teacher);
        }

        return lesson;
    }



    @Override
    @Transactional
    public StudentRegistrationDTO getRegistrationById(Long id) {
        // Tìm thông tin đăng ký dựa trên ID
        StudentRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + id));

        // Chuyển đổi từ entity sang DTO
        return convertToDTO(registration);
    }


    private StudentRegistrationDTO convertToDTO(StudentRegistration registration) {
        return new StudentRegistrationDTO(
                registration.getId(),
                new StudentDTO(
                        registration.getStudent().getId(),
                        registration.getStudent().getStudentName(),
                        registration.getStudent().getEmail(),
                        registration.getStudent().getPhoneNumber(),
                        registration.getStudent().getKakaotalkPhone()
                ),
                registration.getCourse().getCourseName(),
                registration.getStatus().name(),
                registration.getStudentLessons().stream().map(lesson -> new StudentLessonDTO(
                        lesson.getId(),
                        registration.getId(),
                        lesson.getTeacher() != null ? lesson.getTeacher().getId() : null,
                        lesson.getLessonDate(),
                        lesson.getStartTime(),
                        lesson.getEndTime(),
                        lesson.getLessonStatus().name(), // Bao gồm lessonStatus
                        lesson.getNotes()
                )).collect(Collectors.toList())
        );
    }
}
