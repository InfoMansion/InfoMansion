package com.infomansion.server.domain.post.domain;

import com.infomansion.server.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserLikePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_like_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserLikePost(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public static UserLikePost likePost(Post post, User user) {
        return UserLikePost.builder()
                .post(post)
                .user(user)
                .build();
    }
}
