package com.infomansion.server.domain.post.domain;

import com.infomansion.server.domain.base.BaseTimeEntityAtSoftDelete;
import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Where(clause = "delete_flag = false")
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserLikePost> userLikePostList = new ArrayList<>();

    private boolean isPublic;

    private boolean deleteFlag;

    private boolean isPublished;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostImage> imageUrls = new ArrayList<>();

    private Long originPostId;

    @Builder
    public Post(User user, UserStuff userStuff, String title, String content, boolean isPublished) {
        this.title = title;
        this.content = content;
        this.deleteFlag = false;
        this.isPublic = true;
        this.isPublished = isPublished;
        setUserAndUserStuff(user, userStuff);
        replaceDefaultPostThumbnail(content);
    }

    public static Post createPost(User user, UserStuff userStuff, String title, String content) {
        return Post.builder()
                .user(user)
                .userStuff(userStuff)
                .title(title)
                .content(content)
                .isPublished(true)
                .build();
    }

    public static Post createTempPost(User user, String title, String content) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .isPublished(false)
                .build();
    }

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
        replaceDefaultPostThumbnail(content);
    }

    public void updatePostWithUserStuff(UserStuff userStuff, String title, String content) {
        this.userStuff = userStuff;
        this.category = userStuff.getCategory();
        updatePost(title, content);
    }

    public void updateIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void deletePost(S3Uploader s3Uploader){
        Pattern regex = Pattern.compile("<img[^>]*src=[\\\"']?([^>\\\"']+)[\\\"']?[^>]*>");

        List<String> deleteImages = new ArrayList<>();
        Matcher matcher = regex.matcher(this.content);
        while (matcher.find()){
            deleteImages.add(matcher.group(1));
        }
        if(deleteImages.size() > 0)
            s3Uploader.deleteFiles(deleteImages);

        this.deleteFlag = true;
        this.isPublic = false;
        setDeletedDate();
    }

    public void setUserAndUserStuff(User user, UserStuff userStuff){
        this.user = user;
        if(userStuff==null) return;
        this.userStuff = userStuff;
        this.category = userStuff.getCategory();
        this.defaultPostThumbnail = userStuff.getCategory().getCategoryDefaultThumbnail();
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

    public void addUserLikePost(UserLikePost ulp){
        this.userLikePostList.add(ulp);
    }

    public void removeUserLikePost(UserLikePost ulp){
        this.userLikePostList.remove(ulp);
    }

    public String uploadImage(MultipartFile multipartFile, S3Uploader s3Uploader, String username) throws IOException {
        String imageUrl = s3Uploader.uploadFiles(multipartFile, "post/" + username);
        this.imageUrls.add(new PostImage(this, imageUrl));
        return imageUrl;
    }

    public void deleteImage(PostImage postImage) {
        this.imageUrls.remove(postImage);
    }

    public void publish() {
        this.isPublished = true;
    }

    public void linkOriginalPost(Long id) {
        this.originPostId = id;
    }

    /**
     * 사본 만들기
     * 사본을 만들고 원본의 id를 기록해준다.
     * @return
     */
    public Post makeCopy() {
        Post tempPost = Post.builder()
                .user(this.user)
                .userStuff(this.userStuff)
                .title(this.title)
                .content(this.content)
                .isPublished(false)
                .build();
        tempPost.linkOriginalPost(this.id);
        tempPost.copyImageUrls(this);
        return tempPost;
    }

    /**
     * 원본의 imageUrl들을 사본으로 다 복사
     * @param original
     */
    public void copyImageUrls(Post original) {
        List<String> imgUrls = original.getImageUrls().stream().map(PostImage::getImageUrl).collect(Collectors.toList());
        for(String imgUrl : imgUrls) {
            this.imageUrls.add(new PostImage(this, imgUrl));
        }
    }

    public void updateOriginalByCopy(Post copy) {
        this.updatePost(copy.getTitle(), copy.getContent());
        this.copyImageUrls(copy);
    }

    public void changeUserStuff(UserStuff userStuff) {
        this.userStuff = userStuff;
        this.category = userStuff.getCategory();
    }
}
