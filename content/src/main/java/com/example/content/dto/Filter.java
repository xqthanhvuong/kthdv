package com.example.content.dto;

import com.example.content.entity.PostStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filter {
    String keyWord;
    Long userId;
    PostStatus postStatus;
    LocalDate startDay;
    LocalDate endDay;
}
