package com.infomansion.server.domain.post.dto;

import javax.validation.constraints.NotNull;

public class PostRecommendResponseDto {


    @NotNull
    Long userId;

    public PostRecommendResponseDto(Long userId){
        this.userId = userId;
    }
}
