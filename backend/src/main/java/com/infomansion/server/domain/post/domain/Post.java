package com.infomansion.server.domain.post.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
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
public class Post extends BaseTimeEntityAtSoftDelete {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String defaultPostThumbnail;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private LikesPost likesPost;

    private boolean deleteFlag;

    @Builder
    public Post(Long id, User user, UserStuff userStuff, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deleteFlag = false;
        this.likesPost = LikesPost.builder().post(this).build();
        this.defaultPostThumbnail = "default";
        replaceDefaultPostThumbnail(content);
        setUserAndUserStuff(user, userStuff);
    }

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void deletePost(){
        this.deleteFlag = true;
        setDeletedDate();
    }

    public void setUserAndUserStuff(User user, UserStuff userStuff){
        this.user = user;
        this.userStuff = userStuff;
        this.category = userStuff.getCategory();
        userStuff.getPostList().add(this);
    }

    public void replaceDefaultPostThumbnail(String content){
        int startIdx = content.indexOf("<img src=\"");
        if(startIdx==-1) return;
        else{
            startIdx +=10;
            int endIdx = content.indexOf("\"", startIdx);
            String url = content.substring(startIdx, endIdx);
            this.defaultPostThumbnail = url;
        }
    }
}
