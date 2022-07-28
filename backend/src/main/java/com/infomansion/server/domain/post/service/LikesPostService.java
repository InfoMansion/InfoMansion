package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.LikesPostCreateRequestDto;

public interface LikesPostService {

    Long createLikesPost(LikesPostCreateRequestDto requestDto);
    void addLikes(Long postId);
}
