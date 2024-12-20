package com.example.content.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
public enum PostStatus {
    PENDING(0),
    PUBLISH(1),
    REJECT(2),
    BLOCK(3);

    private final int status;

    PostStatus(int status) {
        this.status = status;
    }

    @Converter(autoApply = true)
    public static class PostStatusConverter implements AttributeConverter<PostStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(PostStatus attribute) {
            if (ObjectUtils.isEmpty(attribute)) {
                return null;
            }
            return attribute.getStatus();
        }

        @Override
        public PostStatus convertToEntityAttribute(Integer dbData) {
            if (ObjectUtils.isEmpty(dbData)) {
                return null;
            }
            for (PostStatus status : PostStatus.values()) {
                if (status.getStatus() == dbData) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + dbData);
        }
    }
}
