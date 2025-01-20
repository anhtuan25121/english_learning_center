package com.example.english_learning_center.services;

import com.example.english_learning_center.dtos.StudentDTO;
import com.example.english_learning_center.exceptions.ResourceNotFoundException;
import com.example.english_learning_center.models.Student;
import com.example.english_learning_center.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    //Lấy hoặc tạo mới học sinh dựa trên thông tin StudentDTO.
    @Override
    public StudentDTO getOrCreateStudent(StudentDTO studentDTO) {
        // Kiểm tra email học sinh có tồn tại
        Student student = studentRepository.findByEmail(studentDTO.getEmail())
                .orElseGet(() -> {
                    // Nếu email không tồn tại, tạo mới học sinh
                    Student newStudent = new Student();
                    newStudent.setStudentName(studentDTO.getStudentName());
                    newStudent.setEmail(studentDTO.getEmail());
                    newStudent.setPhoneNumber(studentDTO.getPhoneNumber());
                    newStudent.setKakaotalkPhone(studentDTO.getKakaotalkPhone());
                    return studentRepository.save(newStudent);
                });

        // Nếu email đã tồn tại, cập nhật thông tin học sinh
        student.setStudentName(studentDTO.getStudentName());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setKakaotalkPhone(studentDTO.getKakaotalkPhone());
        studentRepository.save(student);

        return new StudentDTO(
                student.getId(),
                student.getStudentName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getKakaotalkPhone()
        );
    }

    @Override
    public Student getOrCreateStudentEntity(StudentDTO studentDTO) {
        // Kiểm tra xem Student đã tồn tại trong cơ sở dữ liệu chưa
        Student existingStudent = studentRepository.findByEmail(studentDTO.getEmail()).orElse(null);

        if (existingStudent != null) {
            // Cập nhật thông tin nếu tồn tại
            existingStudent.setStudentName(studentDTO.getStudentName());
            existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
            existingStudent.setKakaotalkPhone(studentDTO.getKakaotalkPhone());
            return studentRepository.save(existingStudent);
        }

        // Nếu không tồn tại, tạo mới
        Student newStudent = new Student();
        newStudent.setStudentName(studentDTO.getStudentName());
        newStudent.setEmail(studentDTO.getEmail());
        newStudent.setPhoneNumber(studentDTO.getPhoneNumber());
        newStudent.setKakaotalkPhone(studentDTO.getKakaotalkPhone());
        return studentRepository.save(newStudent);
    }


    //Lấy thông tin học sinh dựa trên email.
   @Override
   public StudentDTO getStudentByEmail(String email) {
       Student student = studentRepository.findByEmail(email)
               .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
       return new StudentDTO(
               student.getId(),
               student.getStudentName(),
               student.getEmail(),
               student.getPhoneNumber(),
               student.getKakaotalkPhone()
       );
   }



}
