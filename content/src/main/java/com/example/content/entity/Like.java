package com.example.content.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;

    // Link to the Posts table via the post_id foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

}
