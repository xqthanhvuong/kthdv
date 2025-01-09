package com.example.content.service;

import com.example.content.constant.ResponseStringConstant;
import com.example.content.dto.*;
import com.example.content.entity.Like;
import com.example.content.entity.Post;
import com.example.content.entity.PostStatus;
import com.example.content.exception.BadException;
import com.example.content.exception.ErrorCode;
import com.example.content.mapper.PostMapper;
import com.example.content.repository.LikeRepository;
import com.example.content.repository.PostRepository;
import com.example.content.until.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    LikeRepository likeRepository;
    PostMapper postMapper;
//    ReportRepository reportRepository;
    ReportService reportService;
    private final UserService userService;

    public PagedResponse<PostResponse> getPostsWithLikeAndCommentCounts(CustomPageRequest<?> request) {
        Pageable pageable = request.toPageable();
        Page<PostResponse> postPage = postRepository
                .findPostsWithLikeAndCommentCounts(pageable, getUserId(request), getPostStatus(request), getKeyWord(request));
        for (PostResponse item: postPage.getContent()) {
            UserResponse userResponse = userService.fetchUserInfo(item.getUserId());
            item.setAvatar(userResponse.getAvatar());
            item.setUserName(userResponse.getUsername());
        }
        Long userId = SecurityUtils.getCurrentUserId();
        return setLikesForPost(setReportForPost(postPage), userId);
    }

    private Page<PostResponse> setReportForPost(Page<PostResponse> postPage) {
        for (PostResponse postResponse : postPage.getContent()) {
            Post post = getPostById(postResponse.getId());
            postResponse.setIsReport(reportService.getIsReport(post.getId()));
        }
        return postPage;
    }

    public List<LikeResponse> getLikesForPost(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new BadException(ErrorCode.POST_NOT_EXISTED);
        }
        List<Long> userIdLikes = likeRepository.findLikesById(postId);
        List<LikeResponse> list = new ArrayList<>();
        for (Long userId: userIdLikes) {
            UserResponse userResponse = userService.fetchUserInfo(userId);
            list.add(LikeResponse.builder()
                            .avatar(userResponse.getAvatar())
                            .name(userResponse.getName())
                    .build());
        }
        return list;
    }

    public void addPost(PostRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        Post post = postMapper.toPost(request);
        post.setUserId(userId);
        post.setPostStatus(PostStatus.PENDING);
        postRepository.save(post);
    }

    public void updatePost(long postId, PostRequest request) {
        Post post = getPostById(postId);

        if (!isOwner(post))
            throw new BadException(ErrorCode.ACCESS_DENIED);

        postMapper.updatePost(post, request);
        postRepository.save(post);
    }

    public void deletePost(long postId) {
        Post post = getPostById(postId);
        if (post.getIsDelete()) {
            throw new BadException(ErrorCode.POST_NOT_EXISTED);
        }
        if (!isOwner(post)) {
            throw new BadException(ErrorCode.ACCESS_DENIED);
        }
        post.setIsDelete(true);
        postRepository.save(post);
    }

    public void blockPost(long postId) {
        Post post = getPostById(postId);
        post.setPostStatus(PostStatus.BLOCK);
        postRepository.save(post);
    }

    public String addLikeForPost(long postId) {
        Post post = getPostById(postId);
        Long userId = SecurityUtils.getCurrentUserId();
        if (post.getPostStatus() != PostStatus.PUBLISH) {
            throw new BadException(ErrorCode.POST_IS_NOT_PUBLISH);
        }
        Like like = new Like();
        if (likeRepository.findLikeByPostAndUserId(post, userId).isPresent()) {
            like = likeRepository.findLikeByPostAndUserId(post, userId).get();
            likeRepository.delete(like);
            return ResponseStringConstant.UNLIKE;
        } else {
            like.setUserId(userId);
            like.setPost(post);
            likeRepository.save(like);
            return ResponseStringConstant.ADD_LIKE;
        }
    }

    private boolean isOwner(@NotNull Post post) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return currentUserId != null && currentUserId.equals(post.getUserId());
    }

    public Post getPostById(long postId) {
        return postRepository.findByIdAndIsDelete(postId, false)
                .orElseThrow(() -> new BadException(ErrorCode.POST_NOT_EXISTED));
    }

    public PagedResponse<PostResponse> setLikesForPost(Page<PostResponse> postPage, Long userId) {
        for (PostResponse postResponse : postPage.getContent()) {
            Post post = getPostById(postResponse.getId());
            if (!ObjectUtils.isEmpty(likeRepository.findLikeByPostAndUserId(post, userId))) {
                postResponse.setIsLiked(true);
            }
        }
        return new PagedResponse<>(
                postPage.getContent(),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast());
    }

    private <T> T getFilterValue(CustomPageRequest<?> request, Function<Filter, T> extractor) {
        Filter filter = (Filter) request.getFilter();
        return (filter != null) ? extractor.apply(filter) : null;
    }

    public String getKeyWord(CustomPageRequest<?> request) {
        return getFilterValue(request, Filter::getKeyWord);
    }

    public Long getUserId(CustomPageRequest<?> request) {
        return getFilterValue(request, filter -> {
            if (ObjectUtils.isEmpty(filter.getUserId())) {
                return null;
            }
            return filter.getUserId();
        });
    }

    public PostStatus getPostStatus(CustomPageRequest<?> request) {
        return getFilterValue(request, Filter::getPostStatus);
    }

    public PostDetailResponse getPostDetail(long postId) {
        return postMapper.toPostDetailResponse(getPostById(postId));
    }





    //admin

    public PagedResponse<?> getPost(CustomPageRequest<?> request) {
        Page<PostResponse> postPage = postRepository
                .findPostsWithLikeAndCommentCountsAdmin(request.toPageable()
                        , getUserId(request)
                        , getPostStatus(request)
                        , getKeyWord(request)
                        , getStartDay(request).atStartOfDay()
                        , getEndDay(request).atTime(LocalTime.MAX));
        for (PostResponse item: postPage.getContent()) {
            UserResponse userResponse = userService.fetchUserInfo(item.getUserId());
            item.setAvatar(userResponse.getAvatar());
            item.setUserName(userResponse.getUsername());
            item.setAuthor(userResponse.getName());
            item.setNumberReport(reportService.getCount(item.getId()));
        }
        return new PagedResponse<>(
                postPage.getContent(),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast());

    }

    public LocalDate getStartDay(CustomPageRequest<?> request) {
        return getFilterValue(request, Filter::getStartDay);
    }

    public LocalDate getEndDay(CustomPageRequest<?> request) {
        return getFilterValue(request, Filter::getEndDay);
    }

    public void rejectPost(Long postId, RejectPostRequest request) {
        Post post = getPostById(postId);
        post.setPostStatus(PostStatus.REJECT);
        post.setRejection_reason(request.getRejectReason());
        postRepository.save(post);
    }

    public void approvalPost(Long postId) {
        Post post = getPostById(postId);
        post.setPostStatus(PostStatus.PUBLISH);
        postRepository.save(post);
    }

    public PagedResponse<?> getReviewPost(CustomPageRequest<?> request) {
        Page<PostResponse> postPage = postRepository
                .findPostsWithLikeAndCommentCountsAdmin(request.toPageable()
                        , getUserId(request)
                        , getPostStatus(request)
                        , getKeyWord(request)
                        , getStartDay(request).atStartOfDay()
                        , getEndDay(request).atTime(LocalTime.MAX));
        for (PostResponse item: postPage.getContent()) {
            UserResponse userResponse = userService.fetchUserInfo(item.getUserId());
            item.setAvatar(userResponse.getAvatar());
            item.setUserName(userResponse.getUsername());
            item.setAuthor(userResponse.getName());
        }
        List<PostReviewResponse> postReviewResponses = new ArrayList<>();
        for (PostResponse post : postPage.getContent()) {
            postReviewResponses.add(postMapper.toPostReviewResponse(post));
        }
        return new PagedResponse<>(
                postReviewResponses,
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast());

    }

    public UserResponse getUserByPost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BadException(ErrorCode.POST_NOT_EXISTED)
        );
        return userService.fetchUserInfo(post.getUserId());
    }

    public PostResponse getPostDetailAdmin(long postId) {
        Post post = getPostById(postId);
        PostResponse postResponse = postRepository.findPostWithLikeAndCommentCounts(post.getId());
        UserResponse userResponse = userService.fetchUserInfo(post.getUserId());
        postResponse.setUserName(userResponse.getUsername());
        postResponse.setAuthor(userResponse.getName());
        postResponse.setAvatar(userResponse.getAvatar());
        postResponse.setNumberReport(reportService.getCount(postId));
        return postResponse;
    }
}
