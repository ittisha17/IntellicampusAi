package com.smartcampus.smartcampus.dto;

import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.Role;
import com.smartcampus.smartcampus.enums.School;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private School school;
    private Program program;
    private Integer year; // null for ADMIN / FACULTY
}