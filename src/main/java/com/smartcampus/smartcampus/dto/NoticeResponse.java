package com.smartcampus.smartcampus.dto;

import com.smartcampus.smartcampus.enums.NoticeCategory;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private NoticeCategory category;
    private School targetSchool;
    private List<Program> targetPrograms;
    private List<Integer> targetYears;
    private String postedBy;
    private LocalDateTime createdAt;
    private boolean acknowledged; // for student's feed view
    private long totalDelivered;
    private long totalAcknowledged;
}