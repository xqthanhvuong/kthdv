package com.example.content.dto;

import com.example.content.exception.ErrorConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectPostRequest {
    @Size(max = 255, message = ErrorConstant.REJECTION_REASON_INVALID)
    @NotBlank(message = ErrorConstant.REJECTION_REASON_INVALID)
    String rejectReason;
}
