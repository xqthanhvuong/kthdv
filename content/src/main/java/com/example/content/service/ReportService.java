package com.example.content.service;

import com.example.content.dto.JsonResponse;
import com.example.content.dto.ReportRequest;
import com.example.content.dto.UserResponse;
import com.example.content.until.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReportService {
    @Value("${report-service.base-url}")
    String reportServiceBaseUrl;

    @Autowired
    RestTemplate restTemplate;

    public boolean getIsReport(Long postId){
        String url = reportServiceBaseUrl + "/reports/get-reports";
        String jwtToken = SecurityUtils.getJwtTokenFromRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportRequest requestBody = ReportRequest.builder()
                .postId(postId)
                .build();

        HttpEntity<ReportRequest> entity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<JsonResponse<Boolean>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<JsonResponse<Boolean>>() {}
            );

            JsonResponse<Boolean> responseBody = response.getBody();
            if (responseBody == null || responseBody.getResult() == null) {
                log.warn("Error response from server");
                return false;
            }

            return responseBody.getResult();

        } catch (Exception e) {
            log.error("Failed to fetch: {}", e.getMessage());
            return false;
        }
    }
}

