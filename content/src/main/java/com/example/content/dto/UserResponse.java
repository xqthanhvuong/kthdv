package com.example.content.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    Long id;
    String username;
    String name;
    String avatar;
    LocalDate dob;
    String liveIn;
    String fromLocation;
    String bio;
}
