package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.entity.DeliveryLog;
import com.smartcampus.smartcampus.dto.NoticeRequest;
import com.smartcampus.smartcampus.dto.NoticeResponse;
import com.smartcampus.smartcampus.entity.Notice;
import com.smartcampus.smartcampus.entity.User;
import com.smartcampus.smartcampus.repository.DeliveryLogRepository;
import com.smartcampus.smartcampus.repository.NoticeRepository;
import com.smartcampus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final DeliveryLogRepository deliveryLogRepository;
    private final DeliveryService deliveryService;

    public NoticeResponse getNoticeById(Long id, String email) {
    Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notice not found!"));

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found!"));

    boolean acknowledged = deliveryLogRepository
            .findByNoticeAndUser(notice, user)
            .map(DeliveryLog::isAcknowledged)
            .orElse(false);

    return mapToResponse(notice, acknowledged);
}

    public NoticeResponse postNotice(NoticeRequest request, String adminEmail) {

        // Find admin user
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found!"));

        // Build notice entity
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setCategory(request.getCategory());
        notice.setTargetSchool(request.getTargetSchool());
        notice.setTargetPrograms(request.getTargetPrograms());
        notice.setTargetYears(request.getTargetYears());
        notice.setPostedBy(admin);

        // Save notice
        Notice saved = noticeRepository.save(notice);

        // Deliver to matching users
        deliveryService.deliverNotice(saved);

        return mapToResponse(saved, false);
    }

    // Student/Faculty sees their own feed
    public List<NoticeResponse> getMyFeed(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return deliveryLogRepository.findByUser(user)
                .stream()
                .map(log -> {
                    NoticeResponse response = mapToResponse(log.getNotice(), log.isAcknowledged());
                    return response;
                })
                .collect(Collectors.toList());
    }

    // Map Notice entity to NoticeResponse DTO
    public NoticeResponse mapToResponse(Notice notice, boolean acknowledged) {
        NoticeResponse response = new NoticeResponse();
        response.setId(notice.getId());
        response.setTitle(notice.getTitle());
        response.setContent(notice.getContent());
        response.setCategory(notice.getCategory());
        response.setTargetSchool(notice.getTargetSchool());
        response.setTargetPrograms(notice.getTargetPrograms());
        response.setTargetYears(notice.getTargetYears());
        response.setPostedBy(notice.getPostedBy().getName());
        response.setCreatedAt(notice.getCreatedAt());
        response.setAcknowledged(acknowledged);
        response.setTotalDelivered(deliveryLogRepository.findByNotice(notice).size());
        response.setTotalAcknowledged(deliveryLogRepository.countByNoticeAndAcknowledgedTrue(notice));
        return response;
    }
}