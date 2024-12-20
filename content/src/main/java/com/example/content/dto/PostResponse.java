package com.example.content.dto;

import com.example.content.entity.PostStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    Long id;
    Long userId;
    String userName;
    String avatar;
    String postTitle;
    String postContent;
    LocalDateTime createdAt;
    PostStatus status;
    Long likeCount;
    Long commentCount;
    Boolean isLiked;
    Boolean isReport;
}
