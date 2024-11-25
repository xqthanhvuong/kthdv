package com.example.user.controller;

import com.example.user.dto.AuthenticationRequest;
import com.example.user.dto.AuthenticationResponse;
import com.example.user.dto.JsonResponse;
import com.example.user.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public JsonResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return JsonResponse.success(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public JsonResponse<String> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request);
        return JsonResponse.success("Logout successful");
    }
}

