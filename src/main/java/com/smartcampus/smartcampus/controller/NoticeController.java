package com.smartcampus.smartcampus.controller;

import com.smartcampus.smartcampus.dto.AudienceSuggestion;
import com.smartcampus.smartcampus.dto.NoticeRequest;
import com.smartcampus.smartcampus.dto.NoticeResponse;
import com.smartcampus.smartcampus.service.AudienceService;
import com.smartcampus.smartcampus.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final AudienceService audienceService;

    // Step 1 — Admin submits notice content, gets AI suggestion back
    @PostMapping("/analyse")
    public ResponseEntity<AudienceSuggestion> analyse(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String content = body.get("content");
        AudienceSuggestion suggestion = audienceService.suggest(title, content);
        return ResponseEntity.ok(suggestion);
    }

    // Step 2 — Admin confirms (with or without edits) and posts
    @PostMapping("/post")
    public ResponseEntity<NoticeResponse> post(@RequestBody NoticeRequest request,
                                                Authentication authentication) {
        String adminEmail = authentication.getName();
        NoticeResponse response = noticeService.postNotice(request, adminEmail);
        return ResponseEntity.ok(response);
    }

    // Student/Faculty sees their personal feed
    @GetMapping("/my-feed")
    public ResponseEntity<List<NoticeResponse>> myFeed(Authentication authentication) {
        String email = authentication.getName();
        List<NoticeResponse> feed = noticeService.getMyFeed(email);
        return ResponseEntity.ok(feed);
    }

    // Get single notice by ID
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getById(@PathVariable Long id,
                                                   Authentication authentication) {
        // Fetch notice and map — acknowledged status depends on user
        String email = authentication.getName();
        NoticeResponse response = noticeService.getNoticeById(id, email);
        return ResponseEntity.ok(response);
    }
}