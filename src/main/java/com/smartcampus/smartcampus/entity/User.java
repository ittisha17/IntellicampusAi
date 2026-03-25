package com.smartcampus.smartcampus.entity;

import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.Role;
import com.smartcampus.smartcampus.enums.School;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private School school;

    @Enumerated(EnumType.STRING)
    private Program program;

    private Integer year;
}