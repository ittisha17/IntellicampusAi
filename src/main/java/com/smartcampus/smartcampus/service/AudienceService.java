package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.dto.AudienceSuggestion;
import com.smartcampus.smartcampus.enums.NoticeCategory;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import com.smartcampus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudienceService {

    private final UserRepository userRepository;

    // Phase 1 — Rule based keyword matching
    // Phase 2 — Replace this with actual AI API call
    public AudienceSuggestion suggest(String title, String content) {

        String text = (title + " " + content).toLowerCase();

        AudienceSuggestion suggestion = new AudienceSuggestion();

        // Step 1 — Suggest Category
        suggestion.setSuggestedCategory(detectCategory(text));

        // Step 2 — Suggest School
        School school = detectSchool(text);
        suggestion.setSuggestedSchool(school);

        // Step 3 — Suggest Programs
        List<Program> programs = detectPrograms(text, school);
        suggestion.setSuggestedPrograms(programs);

        // Step 4 — Suggest Years
        List<Integer> years = detectYears(text);
        suggestion.setSuggestedYears(years);

        // Step 5 — Estimate recipient count
        long count = estimateRecipients(school, programs, years);
        suggestion.setEstimatedRecipients(count);

        // Step 6 — Give reasoning to admin
        suggestion.setReasoning(buildReasoning(text, suggestion));

        return suggestion;
    }

    private NoticeCategory detectCategory(String text) {
        if (containsAny(text, "placement", "hiring", "recruitment", "job", "internship", "offer letter", "drive"))
            return NoticeCategory.PLACEMENT;
        if (containsAny(text, "holiday", "vacation", "closed", "no college", "off day"))
            return NoticeCategory.HOLIDAY;
        if (containsAny(text, "workshop", "seminar", "session", "training", "hands-on"))
            return NoticeCategory.WORKSHOP;
        if (containsAny(text, "research", "paper", "publication", "journal", "conference", "patent"))
            return NoticeCategory.RESEARCH;
        if (containsAny(text, "exam", "test", "assessment", "result", "marks", "grade", "syllabus"))
            return NoticeCategory.ACADEMIC;
        if (containsAny(text, "fest", "event", "celebration", "competition", "hackathon", "cultural"))
            return NoticeCategory.EVENT;
        return NoticeCategory.GENERAL;
    }

    private School detectSchool(String text) {
        boolean hasCST = containsAny(text, "btech", "b.tech", "mca", "bca", "b.sc", "msc",
                                          "computer", "engineering", "software", "coding", "tech");
        boolean hasSOB = containsAny(text, "bba", "mba", "law", "business", "management",
                                          "finance", "marketing", "legal");
        if (hasCST && hasSOB) return School.ALL;
        if (hasSOB) return School.SOB;
        if (hasCST) return School.SCST;
        return School.ALL; // default to all if unclear
    }

    private List<Program> detectPrograms(String text, School school) {
        if (school == School.ALL) {
            return Arrays.asList(Program.values()); // all programs
        }
        if (school == School.SCST) {
            if (containsAny(text, "btech", "b.tech", "undergraduate", "ug"))
                return List.of(Program.BTECH, Program.BCA, Program.BSC);
            if (containsAny(text, "mca", "msc", "postgraduate", "pg"))
                return List.of(Program.MCA, Program.MSC);
            return List.of(Program.BTECH, Program.BCA, Program.BSC, Program.MCA, Program.MSC);
        }
        if (school == School.SOB) {
            if (containsAny(text, "mba", "postgraduate", "pg"))
                return List.of(Program.MBA);
            if (containsAny(text, "law"))
                return List.of(Program.LAW);
            return List.of(Program.BBA, Program.MBA, Program.LAW);
        }
        return List.of(Program.ALL);
    }

    private List<Integer> detectYears(String text) {
        if (containsAny(text, "final year", "4th year", "graduating", "2025 batch", "placement eligible"))
            return List.of(3, 4);
        if (containsAny(text, "first year", "1st year", "fresher", "freshman"))
            return List.of(1);
        if (containsAny(text, "second year", "2nd year"))
            return List.of(2);
        if (containsAny(text, "third year", "3rd year"))
            return List.of(3);
        return List.of(1, 2, 3, 4); // default all years
    }

    private long estimateRecipients(School school, List<Program> programs, List<Integer> years) {
        try {
            if (school == School.ALL) {
                return userRepository.count();
            }
            return userRepository
                    .findBySchoolAndProgramInAndYearIn(school, programs, years)
                    .size();
        } catch (Exception e) {
            return 0;
        }
    }

    private String buildReasoning(String text, AudienceSuggestion suggestion) {
        return String.format(
            "Category '%s' detected. School '%s' identified. " +
            "Targeting %d program(s) and %d year(s).",
            suggestion.getSuggestedCategory(),
            suggestion.getSuggestedSchool(),
            suggestion.getSuggestedPrograms().size(),
            suggestion.getSuggestedYears().size()
        );
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }
}