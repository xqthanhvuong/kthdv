package com.example.content.service;

import com.example.content.dto.CommentRequest;
import com.example.content.dto.CommentResponse;
import com.example.content.dto.UserCommentResponse;
import com.example.content.dto.UserResponse;
import com.example.content.entity.Comment;
import com.example.content.entity.Post;
import com.example.content.entity.PostStatus;
import com.example.content.exception.BadException;
import com.example.content.exception.ErrorCode;
import com.example.content.mapper.CommentMapper;
import com.example.content.repository.CommentRepository;
import com.example.content.until.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    UserService userService;
    PostService postService;

    public List<CommentResponse> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndCommentParentIdIsNullOrderByCreatedAtDesc(postId);
        List<CommentResponse> responses = new ArrayList<>();
        Map<Long, CommentResponse> commentMap = new HashMap<>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
            UserResponse userResponse = userService.fetchUserInfo(comment.getUserId());
            UserCommentResponse userCommentResponse = UserCommentResponse.builder().id(userResponse.getId())
                    .name(userResponse.getName())
                    .avatar(userResponse.getAvatar())
                    .build();
            commentResponse.setUser(userCommentResponse);
            commentMap.put(comment.getId(), commentResponse);
            responses.add(commentResponse);
        }
        comments = commentRepository.findByPostIdAndCommentParentIdIsNotNullOrderByCreatedAtDesc(postId);
        for (Comment comment : comments) {
            CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
            UserResponse userResponse = userService.fetchUserInfo(comment.getUserId());
            UserCommentResponse userCommentResponse = UserCommentResponse.builder().id(userResponse.getId())
                    .name(userResponse.getName())
                    .avatar(userResponse.getAvatar())
                    .build();
            commentResponse.setUser(userCommentResponse);
            commentMap.get(comment.getCommentParentId()).getReplies().add(commentResponse);
        }
        return responses;
    }

    public void addComment(CommentRequest request) throws BadException {
        Long userId = SecurityUtils.getCurrentUserId();
        Post post = postService.getPostById(request.getPostId());
        if (post.getPostStatus() != PostStatus.PUBLISH) {
            throw new BadException(ErrorCode.POST_IS_NOT_PUBLISH);
        }
        if (!ObjectUtils.isEmpty(request.getCommentParentId())) {
            Comment commentParent = commentRepository.findById(request.getCommentParentId())
                    .orElseThrow(() -> new BadException(ErrorCode.COMMENT_NOT_EXISTED));
            if (!ObjectUtils.isEmpty(commentParent.getCommentParentId()))
                throw new BadException(ErrorCode.COMMENT_PARENT_INVALID);
        }
        Comment comment = commentMapper.toComment(request);
        comment.setUserId(userId);
        comment.setPost(post);
        commentRepository.save(comment);
    }
}
