package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;

import javax.validation.constraints.NotNull;

public class PostRecommendResponseDto {


    @NotNull
    Post post;

    public PostRecommendResponseDto(Post post){
        this.post = post;
    }
}
