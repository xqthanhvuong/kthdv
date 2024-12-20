package com.example.content.repository;

import com.example.content.entity.Like;
import com.example.content.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findLikeByPostAndUserId(Post post, Long userId);

    @Query("SELECT l.userId FROM Like l WHERE l.post.id = :postId")
    List<Long> findLikesById(Long postId);
}
