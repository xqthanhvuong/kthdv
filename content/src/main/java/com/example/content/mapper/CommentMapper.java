package com.example.content.mapper;


import com.example.content.dto.CommentRequest;
import com.example.content.dto.CommentResponse;
import com.example.content.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "replies", expression = "java(new java.util.ArrayList<>())")
    CommentResponse toCommentResponse(Comment comment);

    Comment toComment(CommentRequest commentRequest);

}
