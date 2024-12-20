package com.example.content.repository;

import com.example.content.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndCommentParentIdIsNullOrderByCreatedAtDesc(Long postId);

    List<Comment> findByPostIdAndCommentParentIdIsNotNullOrderByCreatedAtDesc(Long postId);
}
