package com.smartcampus.smartcampus.repository;

import com.smartcampus.smartcampus.entity.DeliveryLog;
import com.smartcampus.smartcampus.entity.Notice;
import com.smartcampus.smartcampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, Long> {

    List<DeliveryLog> findByNotice(Notice notice);

    List<DeliveryLog> findByUser(User user);

    Optional<DeliveryLog> findByNoticeAndUser(Notice notice, User user);

    long countByNoticeAndAcknowledgedTrue(Notice notice);
}