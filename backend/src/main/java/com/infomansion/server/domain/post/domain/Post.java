package com.infomansion.server.domain.post.domain;

import com.infomansion.server.domain.base.BaseTimeEntity;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_stuff_id")
    private UserStuff userStuff;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Category category;


    private boolean deleteFlag;

    @Builder
    public Post(Long id, User user, UserStuff userStuff, String title, String content) {
        this.id = id;
        this.user = user;
        this.userStuff = userStuff;
        this.title = title;
        this.content = content;
        this.category = userStuff.getCategory();
        this.deleteFlag = false;
    }

}
