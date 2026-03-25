package com.smartcampus.smartcampus.dto;

import com.smartcampus.smartcampus.enums.NoticeCategory;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import lombok.Data;

import java.util.List;

@Data
public class NoticeRequest {
    private String title;
    private String content;

    // Admin confirms these after seeing AI suggestion
    private NoticeCategory category;
    private School targetSchool;
    private List<Program> targetPrograms;
    private List<Integer> targetYears;
}