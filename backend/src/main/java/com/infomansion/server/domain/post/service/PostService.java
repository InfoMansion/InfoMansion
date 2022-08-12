package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    PostbyUserStuffResponseDto findPostByUserStuffId(Long userStuffId, Pageable pageable);
    PostSearchResponseDto findPostBySearchWordForTitle(String searchWord, Pageable pageable);
    PostSearchResponseDto findPostBySearchWordForContent(String searchWord, Pageable pageable);
    PostDetailResponseDto findPostWithUser(Long postId);
    List<PostSimpleResponseDto> findRecent5ByUser(String userName, Pageable pageable);
    boolean deletePost(Long postId);
    boolean modifyPost(PostModifyRequestDto requestDto);
}

