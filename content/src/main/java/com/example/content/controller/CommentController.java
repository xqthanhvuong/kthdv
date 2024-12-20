package com.example.content.controller;

import com.example.content.constant.ResponseStringConstant;
import com.example.content.dto.CommentRequest;
import com.example.content.dto.CommentResponse;
import com.example.content.dto.JsonResponse;
import com.example.content.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {
    CommentService commentService;

    @GetMapping("/{postId}")
    public JsonResponse<List<CommentResponse>> getComment(@PathVariable Long postId) {
        return JsonResponse.success(commentService.getComments(postId));
    }

    @PostMapping
    public JsonResponse<String> addComment(@RequestBody @Valid CommentRequest request) {
        commentService.addComment(request);
        return JsonResponse.success(ResponseStringConstant.ADD_COMMENT);
    }
}
