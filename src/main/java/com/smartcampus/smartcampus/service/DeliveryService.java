package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.entity.DeliveryLog;
import com.smartcampus.smartcampus.entity.Notice;
import com.smartcampus.smartcampus.entity.User;
import com.smartcampus.smartcampus.enums.School;
import com.smartcampus.smartcampus.repository.DeliveryLogRepository;
import com.smartcampus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryLogRepository deliveryLogRepository;
    private final UserRepository userRepository;

    public void deliverNotice(Notice notice) {

        // Find all matching users based on targeting
        List<User> targetUsers = findTargetUsers(notice);

        // Create a delivery log entry for each user
        for (User user : targetUsers) {
            DeliveryLog log = new DeliveryLog();
            log.setNotice(notice);
            log.setUser(user);
            log.setAcknowledged(false);
            deliveryLogRepository.save(log);
        }
    }

    // Student/Faculty clicks "Got It"
    public void acknowledge(Long noticeId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Notice notice = new Notice();
        notice.setId(noticeId);

        DeliveryLog log = deliveryLogRepository.findByNoticeAndUser(notice, user)
                .orElseThrow(() -> new RuntimeException("Delivery record not found!"));

        log.setAcknowledged(true);
        log.setAcknowledgedAt(LocalDateTime.now());
        deliveryLogRepository.save(log);
    }

    private List<User> findTargetUsers(Notice notice) {
        School school = notice.getTargetSchool();

        // ALL schools — send to everyone
        if (school == School.ALL) {
            return userRepository.findAll();
        }

        // Specific school + programs + years
        if (notice.getTargetPrograms() != null && !notice.getTargetPrograms().isEmpty()
                && notice.getTargetYears() != null && !notice.getTargetYears().isEmpty()) {
            return userRepository.findBySchoolAndProgramInAndYearIn(
                    school,
                    notice.getTargetPrograms(),
                    notice.getTargetYears()
            );
        }

        // Just school filter
        return userRepository.findBySchool(school);
    }
}