package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostDetailResponseDto;
import com.infomansion.server.domain.post.dto.PostSearchResponseDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    List<Long> findRecommendPost();
    List<Long> findRecommendPostByUserLikePost();
    List<PostSimpleResponseDto> findPostByUserStuffId(Long userStuffId);
    PostSearchResponseDto findPostBySearchWordForTitle(String searchWord, Pageable pageable);
    PostSearchResponseDto findPostBySearchWordForContent(String searchWord, Pageable pageable);
    PostDetailResponseDto findPostWithUser(Long postId);
    List<PostSimpleResponseDto> findRecent5ByUser(String userName, Pageable pageable);
}
