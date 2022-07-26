package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostRequestDto;

public interface PostService {

    Long createPost(PostRequestDto requestDto);

}
