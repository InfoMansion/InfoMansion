package com.infomansion.server.domain.post.dto;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostDetailResponseDto {

    private Long id;
    private String userName;
    private String title;
    private String content;
    private Category category;
    private String defaultPostThumbnail;
    private LocalDateTime modifiedDate;
    private Long likes;
    private boolean followFlag;
    private boolean likeFlag;


    public static PostDetailResponseDto toDto(Post post, boolean followFlag, boolean likeFlag){
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .userName(post.getUser().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .defaultPostThumbnail(post.getDefaultPostThumbnail())
                .modifiedDate(post.getModifiedDate())
                .likes((long) post.getUserLikePostList().size())
                .followFlag(followFlag)
                .likeFlag(likeFlag)
                .build();
    }
}
