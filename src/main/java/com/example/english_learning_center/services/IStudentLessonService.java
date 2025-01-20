package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentLessonDTO;
import java.util.List;

public interface IStudentLessonService {


    StudentLessonDTO createLesson(StudentLessonDTO lessonDTO);

    void deleteLesson(Long lessonId);
}
