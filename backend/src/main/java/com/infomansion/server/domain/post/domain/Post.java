package com.infomansion.server.domain.post.domain;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_stuff_id")
    private UserStuff userStuff;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private Long likes;

    private boolean deleteFlag;

    @Builder
    public Post(Long id, UserStuff userStuff, String title, String content) {
        this.id = id;
        this.userStuff = userStuff;
        this.title = title;
        this.content = content;
        this.category = userStuff.getCategory();
        this.likes = 0L;
        this.deleteFlag = false;
    }
}
