package com.smartcampus.smartcampus.entity;

import com.smartcampus.smartcampus.enums.NoticeCategory;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "notices")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private NoticeCategory category;

    @Enumerated(EnumType.STRING)
    private School targetSchool;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Program> targetPrograms;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> targetYears; // e.g. [3, 4]

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}