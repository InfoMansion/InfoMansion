package com.infomansion.server.domain.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRecommendResponseDto {

    private List<Long> userIds;

    public PostRecommendResponseDto(List<Long> userIds){
        this.userIds = userIds;
    }
}
