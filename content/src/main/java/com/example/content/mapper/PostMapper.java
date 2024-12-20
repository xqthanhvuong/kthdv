package com.example.content.mapper;

import com.example.content.dto.PostDetailResponse;
import com.example.content.dto.PostRequest;
import com.example.content.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest postRequest);

    void updatePost(@MappingTarget Post post, PostRequest postRequest);

    PostDetailResponse toPostDetailResponse(Post post);
}
