package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.LikesPost;
import com.infomansion.server.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LikesPostCreateRequestDto {

    @NotNull
    private Long postId;

    @Builder
    public LikesPostCreateRequestDto(Long postId){
        this.postId = postId;
    }

    public LikesPost toEntity(Post post){
        return LikesPost.builder()
                .post(post)
                .build();
    }
}
