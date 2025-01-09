package com.example.content.controller;

import com.example.content.constant.ResponseStringConstant;
import com.example.content.dto.*;
import com.example.content.entity.PostStatus;
import com.example.content.service.PostService;
import com.example.content.until.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @GetMapping
    public JsonResponse<PagedResponse<PostResponse>> getPosts(@RequestParam int page, @RequestParam int size) {
        Filter filter = Filter.builder().postStatus(PostStatus.PUBLISH).build();
        CustomPageRequest<?> customPageRequest = CustomPageRequest.builder().filter(filter).page(page).size(size).build();
        return JsonResponse.success(postService.getPostsWithLikeAndCommentCounts(customPageRequest));
    }

    @GetMapping("/{postId}/likes")
    public JsonResponse<List<LikeResponse>> getLikes(@PathVariable("postId") long postId) {
        return JsonResponse.success(postService.getLikesForPost(postId));
    }

    @PostMapping("/{postId}/likes")
    public JsonResponse<String> addLikes(@PathVariable("postId") long postId) {
        return JsonResponse.success(postService.addLikeForPost(postId));
    }

    @PostMapping
    public JsonResponse<String> addPost(@RequestBody @Valid PostRequest postRequest) {
        postService.addPost(postRequest);
        return JsonResponse.success(ResponseStringConstant.ADD_POST);
    }

    @PutMapping("/{postId}")
    public JsonResponse<String> updatePost(@PathVariable("postId") long postId,
                                           @RequestBody @Valid PostRequest postRequest) {
        postService.updatePost(postId, postRequest);
        return JsonResponse.success(ResponseStringConstant.UPDATE_POST);
    }

    @DeleteMapping("/{postId}")
    public JsonResponse<String> deletePost(@PathVariable("postId") long postId) {
        postService.deletePost(postId);
        return JsonResponse.success(ResponseStringConstant.DELETE_POST);
    }

    @GetMapping("/my-posts")
    public JsonResponse<PagedResponse<PostResponse>> getMyPosts(@RequestParam int page, @RequestParam int size) {
        return getPosts(page, size, null, SecurityUtils.getCurrentUserId(), null);

    }

    @GetMapping("/users/{userId}")
    public JsonResponse<PagedResponse<PostResponse>> getPostsByUserId(@PathVariable("userId") long userId
            , @RequestParam int page
            , @RequestParam int size) {
        return getPosts(page, size, null, userId, PostStatus.PUBLISH);
    }

    @GetMapping("/search")
    public JsonResponse<PagedResponse<PostResponse>> getPostsByKeyword(@RequestParam String keyword
            , @RequestParam int page
            , @RequestParam int size) {
        return getPosts(page, size, keyword, null, PostStatus.PUBLISH);
    }

    @GetMapping("/{postId}")
    public JsonResponse<PostDetailResponse> getPost(@PathVariable("postId") long postId) {
        return JsonResponse.success(postService.getPostDetail(postId));
    }

    @PostMapping("/block/{id}")
    public JsonResponse<String> blockPost(@PathVariable("id") long postId) {
        postService.blockPost(postId);
        return JsonResponse.success("Block Post successfully");
    }

    private JsonResponse<PagedResponse<PostResponse>> getPosts(int page, int size, String keyword, Long userId, PostStatus postStatus) {
        Filter filter = Filter.builder()
                .postStatus(postStatus)
                .keyWord(keyword)
                .userId(userId)
                .build();
        CustomPageRequest<?> customPageRequest = CustomPageRequest.builder()
                .page(page)
                .size(size)
                .filter(filter)
                .build();
        return JsonResponse.success(postService.getPostsWithLikeAndCommentCounts(customPageRequest));
    }


    //admin api

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject-post/{postId}")
    public JsonResponse<String> rejectPost(@PathVariable Long postId, @RequestBody @Valid RejectPostRequest request) {
        postService.rejectPost(postId, request);
        return JsonResponse.success(ResponseStringConstant.REJECT_POST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approval-post/{postId}")
    public JsonResponse<String> approvalPost(@PathVariable Long postId) {
        postService.approvalPost(postId);
        return JsonResponse.success(ResponseStringConstant.APPROVAL_POST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/block-post/{postId}")
    public JsonResponse<String> blockPost(@PathVariable Long postId) {
        postService.blockPost(postId);
        return JsonResponse.success(ResponseStringConstant.BLOCK_POST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public JsonResponse<?> findAllPostsPending(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size
            , @RequestParam(required = false) String keyWord
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay) {
        Filter filter = Filter.builder()
                .keyWord(keyWord)
                .startDay(startDay)
                .endDay(endDay)
                .postStatus(PostStatus.PENDING)
                .build();
        CustomPageRequest<?> customPageRequest = CustomPageRequest.builder()
                .page(page)
                .size(size)
                .filter(filter)
                .build();
        return JsonResponse.success(postService.getReviewPost(customPageRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public JsonResponse<?> findAllPosts(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size
            , @RequestParam(required = false) String keyWord
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay) {
        return findPosts(page, size, keyWord, startDay, endDay, null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reject")
    public JsonResponse<?> findAllPostsReject(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size
            , @RequestParam(required = false) String keyWord
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay) {
        return findPosts(page, size, keyWord, startDay, endDay, PostStatus.REJECT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/publish")
    public JsonResponse<?> findAllPostsPublish(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size
            , @RequestParam(required = false) String keyWord
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay) {
        return findPosts(page, size, keyWord, startDay, endDay, PostStatus.PUBLISH);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/block")
    public JsonResponse<?> findAllPostsBlock(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size
            , @RequestParam(required = false) String keyWord
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay) {
        return findPosts(page, size, keyWord, startDay, endDay, PostStatus.BLOCK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    private JsonResponse<?> findPosts(int page, int size, String keyWord, LocalDate startDay, LocalDate endDay, PostStatus postStatus) {
        Filter filter = Filter.builder()
                .keyWord(keyWord)
                .startDay(startDay)
                .endDay(endDay)
                .postStatus(postStatus)
                .build();
        CustomPageRequest<?> customPageRequest = CustomPageRequest.builder()
                .page(page)
                .size(size)
                .filter(filter)
                .build();
        return JsonResponse.success(postService.getPost(customPageRequest));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-user-by-post/{postId}")
    public JsonResponse<UserResponse> getUserByPost(@PathVariable("postId") long postId) {
        return JsonResponse.success(postService.getUserByPost(postId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{postId}")
    public JsonResponse<PostResponse> getPostAdmin(@PathVariable("postId") long postId) {
        return JsonResponse.success(postService.getPostDetailAdmin(postId));
    }




}
