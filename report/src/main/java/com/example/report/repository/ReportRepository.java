package com.example.report.repository;

import com.example.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostIdAndUserId (Long postId, Long userId);

    Integer countByPostId(Long postId);
}
