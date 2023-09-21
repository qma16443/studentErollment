package com.example.restservice.entitiy;

import jakarta.persistence.*;
import lombok.*;


import java.util.Map;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class Student {
    @Id
    private UUID id;
    private Boolean isEnrolled;
    private String number;
    private String name;
    private String dob;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    @ElementCollection
    @MapKeyColumn(name="class_name")
    @Column(name="subject")
    @CollectionTable(name="student_classes", joinColumns=@JoinColumn(name="student_id"))
    private Map<String, String> classes;


}
