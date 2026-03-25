package com.smartcampus.smartcampus.repository;

import com.smartcampus.smartcampus.entity.Notice;
import com.smartcampus.smartcampus.enums.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByTargetSchoolOrderByCreatedAtDesc(School school);

    List<Notice> findAllByOrderByCreatedAtDesc();
}