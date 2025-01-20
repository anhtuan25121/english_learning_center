package com.example.english_learning_center.repositories;

import com.example.english_learning_center.models.Teacher;
import com.example.english_learning_center.models.TeacherSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TeacherSlotRepository extends JpaRepository<TeacherSlot, Long> {
    List<TeacherSlot> findByTeacher(Teacher teacher);

    // Lấy danh sách các timeslot có giáo viên rảnh trong ngày
    @Query("SELECT ts FROM TeacherSlot ts WHERE ts.availabilityDate = :availabilityDate")
    List<TeacherSlot> findAvailableTimeSlotsByDate(LocalDate availabilityDate);

    // Lấy danh sách giáo viên rảnh trong timeslot cụ thể
    @Query("SELECT ts.teacher.id FROM TeacherSlot ts WHERE ts.availabilityDate = :availabilityDate AND ts.startTime = :startTime AND ts.endTime = :endTime")
    List<Long> findAvailableTeachersByTimeSlot(LocalDate availabilityDate, String startTime, String endTime);


    @Query("SELECT t.teacher.id FROM TeacherSlot t WHERE t.availabilityDate = :availabilityDate AND t.startTime = :startTime AND t.endTime = :endTime")
    List<Long> findAvailableTeacherIds(@Param("availabilityDate") LocalDate availabilityDate,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime);

    @Query("SELECT ts.teacher FROM TeacherSlot ts WHERE ts.availabilityDate = :availabilityDate AND ts.startTime = :startTime AND ts.endTime = :endTime")
    List<Teacher> findAvailableTeachersByDateAndTime(@Param("availabilityDate") LocalDate availabilityDate,
                                                     @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime);

    @Query("SELECT DISTINCT t.availabilityDate, t.startTime, t.endTime " +
            "FROM TeacherSlot t " +
            "WHERE t.availabilityDate = :date")
    List<Object[]> findDistinctTimeSlotsByDate(@Param("date") LocalDate date);


    @Query("SELECT ts.teacher.teacherName " +
            "FROM TeacherSlot ts " +
            "WHERE ts.availabilityDate = :availabilityDate " +
            "AND ts.startTime = :startTime " +
            "AND ts.endTime = :endTime " +
            "AND ts.course.courseName = :courseName")
    List<String> findAvailableTeacherNamesByCourse(
            @Param("availabilityDate") LocalDate availabilityDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("courseName") String courseName
    );

}