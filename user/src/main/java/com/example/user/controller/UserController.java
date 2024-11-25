package com.example.user.controller;


import com.example.user.dto.JsonResponse;
import com.example.user.dto.UserResponse;
import com.example.user.dto.UserSignUpRequest;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public JsonResponse<String> createUser(@RequestBody @Valid UserSignUpRequest request) {
        userService.signUp(request);
        return JsonResponse.success("User created successfully!");
    }

    @GetMapping("/{userId}")
    public JsonResponse<UserResponse> getUserById(@PathVariable Long userId) {
        return JsonResponse.success(userService.getUserById(userId));
    }

    @GetMapping("/my-info")
    public JsonResponse<UserResponse> getMyInfo() {
        return JsonResponse.success(userService.getMyInfo());
    }
}

