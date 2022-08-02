package com.infomansion.server.domain.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class LikesPost {

    @Id
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post  post;

    private Long likes;

    @Builder
    public LikesPost(Post post) {
        this.post = post;
        this.likes = 0L;
    }

    public void addPostLikes(){
        likes++;
    }
}
