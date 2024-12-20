package com.example.content.repository;

import com.example.content.dto.LikeResponse;
import com.example.content.dto.PostResponse;
import com.example.content.entity.Post;
import com.example.content.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndIsDelete(Long id, Boolean isDelete);

    @Query("SELECT new com.example.content.dto.PostResponse" +
            "(p.id, p.userId, null , null , p.postTitle, p.postContent" +
            ", p.createdAt, p.postStatus, COUNT(DISTINCT l.id), COUNT(DISTINCT c.id), false, false ) " +
            "FROM Post p LEFT JOIN p.likes l LEFT JOIN p.comments c " +
            "WHERE p.isDelete = false " +
            "and (:status IS NULL OR p.postStatus = :status) " +
            "and (:userId IS NULL OR p.userId = :userId) " +
            "and (:keyword IS NULL OR (lower(p.postTitle) like lower(concat('%', :keyword, '%')) " +
            "or lower(p.postContent) like lower(concat('%', :keyword, '%')))) " +
            "GROUP BY p.id")
    Page<PostResponse> findPostsWithLikeAndCommentCounts(Pageable pageable, @Param("userId") Long userId, @Param("status") PostStatus status, @Param("keyword") String keyWord);

}
