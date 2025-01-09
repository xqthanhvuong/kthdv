package com.example.report.repository;

import com.example.report.dto.response.ReportResponse;
import com.example.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostIdAndUserId (Long postId, Long userId);

    Integer countByPostId(Long postId);

    @Query("SELECT new com.example.report.dto.response.ReportResponse(null ,null , r.reportContent) FROM Report r WHERE r.postId = :postId")
    List<ReportResponse> findReport(@Param("postId") Long postId);
}
