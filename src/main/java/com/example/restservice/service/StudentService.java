package com.example.restservice.service;

import com.example.restservice.entitiy.Student;
import com.example.restservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    // Auto-incremented 7 digits number for student, First Student number will be 1
    private static Integer studentNumber = 0;

    public Student getStudent(UUID id) {
        return studentRepository.findById(id).orElse(null);
    }

    //student Validation
    void validateStudent(Student student) throws IllegalArgumentException {
        validateMandatoryFields(student);
        validateEnrollmentStatus(student);
    }

    private void validateMandatoryFields(Student student) throws IllegalArgumentException {
        String[] mandatoryFields = {"Name", "Date of birth", "Address Line 1", "City", "State", "ZIP code", "Phone number"};
        String[] values = {
                student.getName(),
                student.getDob(),
                student.getAddressLine1(),
                student.getCity(),
                student.getState(),
                student.getZip(),
                student.getPhoneNumber()
        };

        for (int i = 0; i < mandatoryFields.length; i++) {
            if (values[i] == null || values[i].isEmpty()) {
                throw new IllegalArgumentException(mandatoryFields[i] + " cannot be null or empty");
            }
        }
    }

    private void validateEnrollmentStatus(Student student) throws IllegalArgumentException {
        if (student.getIsEnrolled() && (student.getClasses() == null || student.getClasses().size() != 3)) {
            throw new IllegalArgumentException("Enrolled students must be enrolled in exactly 3 classes");
        }
    }

    public Student createStudent(Student student) {
        student.setId(UUID.randomUUID());
        // Increment student number
        if (String.valueOf(studentNumber).length() > 7) {
            throw new IllegalArgumentException("Student number should not exceed 7 digits");
        }
        studentNumber++;
        // Format it as a 7-digit number
        String formattedStudentNumber = String.format("%07d", studentNumber);
        student.setNumber(formattedStudentNumber);
        validateStudent(student);

        return studentRepository.save(student);

    }

    public Student updateStudent(UUID id, Student newStudentData) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();

            // Only update the fields that are present in the newStudentData object
            if (newStudentData.getName() != null) {
                existingStudent.setName(newStudentData.getName());
            }
            if (newStudentData.getDob() != null) {
                existingStudent.setDob(newStudentData.getDob());
            }
            if (newStudentData.getAddressLine1() != null) {
                existingStudent.setAddressLine1(newStudentData.getAddressLine1());
            }
            if (newStudentData.getAddressLine2() != null) {
                existingStudent.setAddressLine2(newStudentData.getAddressLine2());
            }
            if (newStudentData.getCity() != null) {
                existingStudent.setCity(newStudentData.getCity());
            }
            if (newStudentData.getState() != null) {
                existingStudent.setState(newStudentData.getState());
            }
            if (newStudentData.getZip() != null) {
                existingStudent.setZip(newStudentData.getZip());
            }
            if (newStudentData.getPhoneNumber() != null) {
                existingStudent.setPhoneNumber(newStudentData.getPhoneNumber());
            }
            if (newStudentData.getIsEnrolled() != null) {
                existingStudent.setIsEnrolled(newStudentData.getIsEnrolled());
            }
            if (newStudentData.getClasses() != null) {
                existingStudent.setClasses(newStudentData.getClasses());
            }

            // Validation of updated student
            validateStudent(existingStudent);

            // Save the updated student back to database
            return studentRepository.save(existingStudent);
        } else {
            throw new RuntimeException("Student with ID " + id + " not found");
        }
    }


    public void deleteStudent(UUID id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Student with ID " + id + " not found");
        }
    }
}

