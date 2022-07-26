package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class PostRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long userStuffId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public PostRequestDto(Long userStuffId, String title, String content) {
        this.userStuffId = userStuffId;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(UserStuff userStuff){
        return Post.builder()
                .userStuff(userStuff)
                .title(title)
                .content(content)
                .build();
    }
}
