package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    List<PostRecommendResponseDto> findRecommendPost(Long userId);
}
