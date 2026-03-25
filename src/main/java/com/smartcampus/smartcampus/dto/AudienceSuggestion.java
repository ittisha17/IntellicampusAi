package com.smartcampus.smartcampus.dto;

import com.smartcampus.smartcampus.enums.NoticeCategory;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import lombok.Data;

import java.util.List;

@Data
public class AudienceSuggestion {

    // AI suggested values currently rule based (Phase 1 = rule based, Phase 2 = actual AI)
    private NoticeCategory suggestedCategory;
    private School suggestedSchool;
    private List<Program> suggestedPrograms;
    private List<Integer> suggestedYears;

    // Preview info for admin
    private long estimatedRecipients;
    private String reasoning; // "Found keywords: placement, TCS, hiring"
}