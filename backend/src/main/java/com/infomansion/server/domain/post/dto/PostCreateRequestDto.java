package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long userStuffId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public PostCreateRequestDto(Long userId, Long userStuffId, String title, String content) {
        this.userId = userId;
        this.userStuffId = userStuffId;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(User user, UserStuff userStuff){
        return Post.builder()
                .user(user)
                .userStuff(userStuff)
                .title(title)
                .content(content)
                .build();
    }
}
