package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    PostRecommendResponseDto findRecommendPost();
}
