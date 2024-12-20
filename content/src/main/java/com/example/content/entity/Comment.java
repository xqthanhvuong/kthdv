package com.example.content.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob  // used for TEXT data type
    @Column(nullable = false)
    String commentContent;

    @Column
    Long commentParentId;  // ID of the parent comment, if any

    //Link to the Posts table via the post_id foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    Long userId;

    @Column(nullable = false)
    Boolean isDelete;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        isDelete = false;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
