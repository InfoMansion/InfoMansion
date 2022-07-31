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
    @Column(name ="post_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post  post;

    private Long likes;

    @Builder
    public LikesPost(Long id, Post post) {
        this.id = id;
        this.post = post;
        this.likes = 0L;
    }

    public void addPostLikes(){
        likes++;
    }
}
