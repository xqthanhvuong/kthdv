package com.example.user.dto;

import com.example.user.exception.ErrorConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = ErrorConstant.NAME_INVALID)
    @Size(min = 2, max = 50, message = ErrorConstant.NAME_INVALID)
    String name;
    String avatar;
    LocalDate dob;
    @Size(max = 255, message = ErrorConstant.INVALID_LOCATION)
    String liveIn;
    @Size(max = 255, message = ErrorConstant.INVALID_LOCATION)
    String fromLocation;
    String bio;
}
