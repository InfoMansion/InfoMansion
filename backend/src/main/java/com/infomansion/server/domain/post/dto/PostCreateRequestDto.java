package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private Long userStuffId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<String> images = new ArrayList<>();

    @Builder
    public PostCreateRequestDto(Long userStuffId, String title, String content, List<String> images) {
        this.userStuffId = userStuffId;
        this.title = title;
        this.content = content;
        this.images = images;
    }

    public Post toEntity(User user, UserStuff userStuff){
        return Post.createPost(user, userStuff, title, content);
    }
}
