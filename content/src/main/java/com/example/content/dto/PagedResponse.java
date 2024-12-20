package com.example.content.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedResponse<T> {
    List<T> content;
    int currentPage;
    long totalItems;
    int totalPages;
    boolean isLast;
}
