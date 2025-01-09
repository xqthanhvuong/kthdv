package com.example.content.dto;

import com.example.content.entity.PostStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReviewResponse {
    Long id;
    Long userId;
    String author;
    String postTitle;
    String postContent;
    LocalDateTime createdAt;
    PostStatus status;
}
