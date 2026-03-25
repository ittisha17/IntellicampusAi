package com.smartcampus.smartcampus.controller;

import com.smartcampus.smartcampus.entity.DeliveryLog;
import com.smartcampus.smartcampus.repository.DeliveryLogRepository;
import com.smartcampus.smartcampus.repository.NoticeRepository;
import com.smartcampus.smartcampus.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryLogRepository deliveryLogRepository;
    private final NoticeRepository noticeRepository;

    // Student/Faculty clicks "Got It"
    @PostMapping("/acknowledge/{noticeId}")
    public ResponseEntity<String> acknowledge(@PathVariable Long noticeId,
                                               Authentication authentication) {
        String email = authentication.getName();
        deliveryService.acknowledge(noticeId, email);
        return ResponseEntity.ok("Acknowledged successfully!");
    }

    // Admin sees delivery log for a notice
    @GetMapping("/log/{noticeId}")
    public ResponseEntity<List<DeliveryLog>> getLog(@PathVariable Long noticeId) {
        return noticeRepository.findById(noticeId)
                .map(notice -> ResponseEntity.ok(deliveryLogRepository.findByNotice(notice)))
                .orElse(ResponseEntity.notFound().build());
    }
}