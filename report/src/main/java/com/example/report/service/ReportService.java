package com.example.report.service;

import com.example.report.dto.request.ReportRequest;
import com.example.report.dto.response.JsonResponse;
import com.example.report.entity.Report;
import com.example.report.repository.ReportRepository;
import com.example.report.until.SecurityUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportService {
    @Value("${post-service.base-url}")
    String postServiceBaseUrl;
    @Autowired
    ReportRepository reportRepository;
    RestTemplate restTemplate;


    public void save(ReportRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        Report report = new Report();
        report.setReportContent(request.getReportContent());
        report.setUserId(userId);
        report.setPostId(request.getPostId());
        reportRepository.save(report);
        if (reportRepository.countByPostId(request.getPostId()) + 1 >= 5) {
            blockPost(report.getPostId());
        }
    }

    private void blockPost(Long postId) {
        String url = postServiceBaseUrl + "/posts/block/" + postId;
        String jwtToken = SecurityUtils.getJwtTokenFromRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<JsonResponse<String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<JsonResponse<String>>() {}
            );

            JsonResponse<String> responseBody = response.getBody();
            if (responseBody == null || responseBody.getResult() == null) {
                log.warn("Error blocking post: {}", responseBody);
            }
        } catch (Exception e) {
            log.error("Failed to block {}", e.getMessage());
        }

    }

    public boolean isReported(Long postId) {
        List<Report> reports = reportRepository.findByPostIdAndUserId(postId, SecurityUtils.getCurrentUserId());
        if (!reports.isEmpty()) {
            return true;
        }else {
            return false;
        }
    }
}
