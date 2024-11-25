package com.example.user.mapper;

import com.example.user.dto.UserResponse;
import com.example.user.dto.UserSignUpRequest;
import com.example.user.dto.UserUpdateRequest;
import com.example.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserSignUpRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
