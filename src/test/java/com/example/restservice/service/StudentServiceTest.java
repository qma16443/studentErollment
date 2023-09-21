package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.restservice.entitiy.Student;
import com.example.restservice.repository.StudentRepository;
import com.example.restservice.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // Positive Test Case for getStudent
    @Test
    public void testGetStudent_found() {
        UUID uuid = UUID.randomUUID();
        Student student = new Student();
        student.setId(uuid);

        when(studentRepository.findById(uuid)).thenReturn(Optional.of(student));

        Student result = studentService.getStudent(uuid);
        assertEquals(uuid, result.getId());
    }

    // Negative Test Case for getStudent
    @Test
    public void testGetStudent_notFound() {
        UUID uuid = UUID.randomUUID();
        when(studentRepository.findById(uuid)).thenReturn(Optional.empty());

        Student result = studentService.getStudent(uuid);
        assertNull(result);
    }

    // Positive Test Case for createStudent
    @Test
    public void testCreateStudent_valid() {
        Student student = new Student();
        student.setName("John");
        student.setDob("2000-01-01");
        student.setAddressLine1("123 Main St");
        student.setCity("Springfield");
        student.setState("IL");
        student.setZip("62704");
        student.setPhoneNumber("555-5555");
        student.setIsEnrolled(false);
        // Assume that student.setClasses() is properly set with exactly 3 classes

        Student savedStudent = new Student();
        savedStudent.setId(UUID.randomUUID());
        savedStudent.setName("John");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.createStudent(student);

        assertNotNull(result.getId());
        assertEquals("John", result.getName());
        verify(studentRepository, times(1)).save(student);
    }


    // Negative Test Case for createStudent
    @Test
    public void testCreateStudent_invalid() {
        Student student = new Student();

        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
    }

    // Positive Test Case for updateStudent
    @Test
    public void testUpdateStudent_valid() {
        UUID uuid = UUID.randomUUID();
        Student existingStudent = new Student();
        existingStudent.setName("John");
        existingStudent.setDob("2000-01-01");
        existingStudent.setAddressLine1("123 Main St");
        existingStudent.setCity("Springfield");
        existingStudent.setState("IL");
        existingStudent.setZip("62704");
        existingStudent.setPhoneNumber("555-5555");
        existingStudent.setIsEnrolled(false);
        Student newStudentData = new Student();
        newStudentData.setName("John2");

        when(studentRepository.findById(uuid)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        Student result = studentService.updateStudent(uuid, newStudentData);
        assertEquals("John2", result.getName());
    }

    // Negative Test Case for updateStudent
    @Test
    public void testUpdateStudent_notFound() {
        UUID uuid = UUID.randomUUID();
        Student newStudentData = new Student();

        when(studentRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(uuid, newStudentData));
    }

    // Positive Test Case for deleteStudent
    @Test
    public void testDeleteStudent_valid() {
        UUID uuid = UUID.randomUUID();
        Student existingStudent = new Student();

        when(studentRepository.findById(uuid)).thenReturn(Optional.of(existingStudent));
        doNothing().when(studentRepository).deleteById(uuid);

        assertDoesNotThrow(() -> studentService.deleteStudent(uuid));
    }

    // Negative Test Case for deleteStudent
    @Test
    public void testDeleteStudent_notFound() {
        UUID uuid = UUID.randomUUID();
        when(studentRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.deleteStudent(uuid));
    }

    @Test
    public void testEnrollmentWithThreeClasses() {
        Student existingStudent = new Student();
        existingStudent.setName("John");
        existingStudent.setDob("2000-01-01");
        existingStudent.setAddressLine1("123 Main St");
        existingStudent.setCity("Springfield");
        existingStudent.setState("IL");
        existingStudent.setZip("62704");
        existingStudent.setPhoneNumber("555-5555");
        existingStudent.setIsEnrolled(false);
        existingStudent.setIsEnrolled(true);
        Map<String, String> classes = new HashMap<>();
        classes.put("Class1", "Algebra");
        classes.put("Class2", "Physics");
        classes.put("Class3", "World History");
        existingStudent.setClasses(classes);

        // No exception should be thrown
        assertDoesNotThrow(() -> studentService.validateStudent(existingStudent));
    }

    @Test
    public void testEnrollmentWithLessThanThreeClasses() {
        Student existingStudent = new Student();
        existingStudent.setName("John");
        existingStudent.setDob("2000-01-01");
        existingStudent.setAddressLine1("123 Main St");
        existingStudent.setCity("Springfield");
        existingStudent.setState("IL");
        existingStudent.setZip("62704");
        existingStudent.setPhoneNumber("555-5555");
        existingStudent.setIsEnrolled(false);
        existingStudent.setIsEnrolled(true);
        existingStudent.setIsEnrolled(true);
        Map<String, String> classes = new HashMap<>();
        classes.put("Class1", "Algebra");
        classes.put("Class2", "Physics");
        existingStudent.setClasses(classes);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> studentService.validateStudent(existingStudent));
        assertEquals("Enrolled students must be enrolled in exactly 3 classes", exception.getMessage());
    }


}
