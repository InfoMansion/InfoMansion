package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;
import com.infomansion.server.domain.post.dto.PostSearchResponseDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    PostRecommendResponseDto findRecommendPost();
    List<PostSimpleResponseDto> findPostByUserStuffId(Long userStuffId);
    PostSearchResponseDto findPostBySearchWord(String searchWord, Pageable pageable);
}
