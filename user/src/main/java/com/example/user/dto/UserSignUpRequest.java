package com.example.user.dto;

import com.example.user.exception.ErrorConstant;
import com.example.user.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    @Email(message = ErrorConstant.USERNAME_INVALID)
    String username;
    @ValidPassword
    String password;
    @NotBlank(message = ErrorConstant.NAME_INVALID)
    @Size(min = 2, max = 50, message = ErrorConstant.NAME_INVALID)
    String name;
    String avatar;
}

