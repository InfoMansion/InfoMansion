package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostCreateRequestDto;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);

}
