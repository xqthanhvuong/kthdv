package com.example.content.dto;

import com.example.content.exception.ErrorConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
    @Size(max = 600, message = ErrorConstant.POST_TITLE_INVALID)
    @NotBlank(message = ErrorConstant.POST_TITLE_INVALID)
    String postTitle;
    @Size(max = 2000, message = ErrorConstant.POST_CONTENT_INVALID)
    @NotBlank(message = ErrorConstant.POST_CONTENT_INVALID)
    String postContent;
}
