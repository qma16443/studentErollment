package com.example.restservice.repository;
import com.example.restservice.entitiy.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StudentRepository extends CrudRepository<Student, UUID> {
}

