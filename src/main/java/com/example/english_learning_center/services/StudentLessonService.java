package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentLessonDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.StudentLesson;
import com.example.english_learning_center.models.StudentRegistration;
import com.example.english_learning_center.repositories.StudentLessonRepository;
import com.example.english_learning_center.repositories.StudentRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentLessonService implements IStudentLessonService {

    private final StudentLessonRepository lessonRepository;
    private final StudentRegistrationRepository registrationRepository;



    @Override
    public StudentLessonDTO createLesson(StudentLessonDTO lessonDTO) {
        // Kiểm tra StudentRegistration tồn tại
        StudentRegistration registration = registrationRepository.findById(lessonDTO.getStudentRegistrationId())
                .orElseThrow(() -> new ResourceNotFoundException("Student Registration not found with ID: " + lessonDTO.getStudentRegistrationId()));

        // Tạo mới buổi học
        StudentLesson lesson = new StudentLesson();
        lesson.setStudentRegistration(registration);
        lesson.setLessonDate(lessonDTO.getLessonDate());
        lesson.setStartTime(lessonDTO.getStartTime());
        lesson.setEndTime(lessonDTO.getEndTime());
        lesson.setTeacherId(lessonDTO.getTeacherId());
        lesson.setLessonStatus(StudentLesson.LessonStatus.upcoming);

        lesson = lessonRepository.save(lesson);

        return convertToDTO(lesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new ResourceNotFoundException("Lesson not found with ID: " + lessonId);
        }
        lessonRepository.deleteById(lessonId);
    }

    //Chuyển đổi Entity thành DTO
    private StudentLessonDTO convertToDTO(StudentLesson lesson) {
        return new StudentLessonDTO(
                lesson.getId(),
                lesson.getStudentRegistration().getId(),
                lesson.getTeacherId(),
                lesson.getLessonDate(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getLessonStatus().name(),
                lesson.getNotes()
        );
    }
}
