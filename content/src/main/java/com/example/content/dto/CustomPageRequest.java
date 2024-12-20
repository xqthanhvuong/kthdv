package com.example.content.dto;

import com.example.content.constant.FilterConstant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomPageRequest<T> {
    int page;
    int size;
    T filter;
    String sortBy;
    String direction;

    public Pageable toPageable() {
        if (ObjectUtils.isEmpty(sortBy)) {
            sortBy = FilterConstant.CREATE_AT;
        }
        if (ObjectUtils.isEmpty(direction)) {
            direction = FilterConstant.DESC;
        }
        Sort.Direction dir = Sort.Direction.fromString(direction);
        return org.springframework.data.domain.PageRequest.of(page, size, Sort.by(dir, sortBy));
    }
}
