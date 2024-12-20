package com.example.content.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 600)
    String postTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    String postContent;

    @Column(length = 255)
    String rejection_reason;

    @Column
    @Enumerated(EnumType.ORDINAL)
    PostStatus postStatus;

    Long userId;

    @Column(nullable = false)
    Boolean isDelete;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post")
    Set<Comment> comments;

    @OneToMany(mappedBy = "post")
    Set<Like> likes;


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
