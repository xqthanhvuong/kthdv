package com.example.content.controller;

import com.example.content.constant.ResponseStringConstant;
import com.example.content.dto.*;
import com.example.content.entity.PostStatus;
import com.example.content.service.PostService;
import com.example.content.until.SecurityUtils;
import io.swagger.v3.core.util.Json;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

}
