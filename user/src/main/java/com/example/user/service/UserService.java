package com.example.user.service;

import com.example.user.dto.UserResponse;
import com.example.user.dto.UserSignUpRequest;
import com.example.user.dto.UserUpdateRequest;
import com.example.user.entity.User;
import com.example.user.exception.BadException;
import com.example.user.exception.ErrorCode;
import com.example.user.mapper.UserMapper;
import com.example.user.repository.UserRepository;
import com.example.user.until.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public void signUp(UserSignUpRequest request) {
        if (userRepository.existsUserByUsernameAndIsDelete(request.getUsername(), false)) {
            throw new BadException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public UserResponse getUserById(Long userId) {
        return userMapper.toUserResponse(getUser(userId));
    }

    public User getUser(Long userId) {
        return userRepository.findByIdAndIsDelete(userId, false)
                .orElseThrow(() -> new BadException(ErrorCode.USER_NOT_EXISTED));
    }

    public UserResponse getMyInfo() {
        String name = SecurityUtils.getCurrentUsername();
        var user = userRepository.findByUsernameAndIsDelete(name, false)
                .orElseThrow(() -> new BadException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public User getCurrentUser() {
        return userRepository.findByUsernameAndIsDelete(SecurityUtils.getCurrentUsername(), false)
                .orElseThrow(() -> new BadException(ErrorCode.USER_NOT_EXISTED));
    }

    public void updateUser(UserUpdateRequest request) {
        User user = getCurrentUser();
        userMapper.updateUser(user, request);
        userRepository.save(user);
    }
}
