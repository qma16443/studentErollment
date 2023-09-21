package com.example.restservice.controller;

import com.example.restservice.entitiy.Student;
import com.example.restservice.service.StudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/studentEnrollment")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/{uuid}", produces = "application/json")
    @ApiOperation("Get a student record by UUID")
    public Student getStudent(@ApiParam(value = "UUID of the student to retrieve", required = true) @PathVariable("uuid") UUID id) {
        return studentService.getStudent(id);
    }

    @PutMapping(value = "/{uuid}", consumes = "application/json", produces = "application/json")
    @ApiOperation("Update an existing student record")
    public Student updateStudent(@ApiParam(value = "UUID of the student to update", required = true) @PathVariable("uuid") UUID id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    @ApiOperation("Create a new student record")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @DeleteMapping(value = "/{uuid}", produces = "application/json")
    @ApiOperation("Delete a student record")
    public void deleteStudent(@ApiParam(value = "UUID of the student to delete", required = true) @PathVariable("uuid") UUID id) {
        studentService.deleteStudent(id);
    }
}
