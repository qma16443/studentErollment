package com.example.restservice.controller;

import com.example.restservice.entitiy.Student;
import com.example.restservice.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    public void testGetStudent() throws Exception {
        UUID id = UUID.randomUUID();
        Student student = new Student();
        student.setId(id);

        when(studentService.getStudent(id)).thenReturn(student);

        mockMvc.perform(get("/studentEnrollment/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setId(UUID.randomUUID());

        when(studentService.createStudent(any())).thenReturn(student);

        mockMvc.perform(post("/studentEnrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student();
        UUID id = UUID.randomUUID();
        student.setId(id);

        when(studentService.updateStudent(Mockito.eq(id), any())).thenReturn(student);

        mockMvc.perform(put("/studentEnrollment/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/studentEnrollment/" + id))
                .andExpect(status().isOk());

        Mockito.verify(studentService, Mockito.times(1)).deleteStudent(id);
    }

    @Test
    public void testGetStudentNotFound() throws Exception {
        when(studentService.getStudent(any(UUID.class))).thenReturn(null);
        mockMvc.perform(get("/studentEnrollment/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testCreateStudentValidationError() throws Exception {
        when(studentService.createStudent(any())).thenThrow(new IllegalArgumentException("Validation Error"));
        Student student = new Student();  // Invalid student data
        mockMvc.perform(post("/studentEnrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStudentNotFound() throws Exception {
        when(studentService.updateStudent(any(UUID.class), any())).thenReturn(null);
        Student student = new Student();  // Student data to update
        mockMvc.perform(put("/studentEnrollment/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteStudentNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Student not found")).when(studentService).deleteStudent(any(UUID.class));
        mockMvc.perform(delete("/studentEnrollment/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

}
