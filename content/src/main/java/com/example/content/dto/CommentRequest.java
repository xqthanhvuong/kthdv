package com.example.content.dto;

import com.example.content.exception.ErrorConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommentRequest {
    @NotBlank(message = ErrorConstant.COMMENT_INVALID)
    String commentContent;
    Long commentParentId;
    @NotNull(message = ErrorConstant.MISSING_POST_ID)
    Long postId;
}
