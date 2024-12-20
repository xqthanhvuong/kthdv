package com.example.content.service;

import com.example.content.dto.JsonResponse;
import com.example.content.dto.UserResponse;
import com.example.content.until.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserService {

    @Value("${user-service.base-url}")
    String userServiceBaseUrl;

    @Autowired
    RestTemplate restTemplate;

    public UserResponse fetchUserInfo(Long userId) {
        String url = userServiceBaseUrl + "/users/" + userId;
        String jwtToken = SecurityUtils.getJwtTokenFromRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonResponse<UserResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<JsonResponse<UserResponse>>() {}
            );

            JsonResponse<UserResponse> responseBody = response.getBody();
            if (responseBody == null || responseBody.getResult() == null) {
                log.warn("User service response is null or missing result for userId {}", userId);
                return UserResponse.builder().name("Unknown").build();
            }

            return responseBody.getResult();

        } catch (Exception e) {
            log.error("Failed to fetch user info for userId {}: {}", userId, e.getMessage());
            return UserResponse.builder().name("Unknown").build();
        }
    }
}
