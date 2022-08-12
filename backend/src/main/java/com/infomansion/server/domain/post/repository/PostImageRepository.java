package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findPostImagesByImageUrlIsIn(List<String> deleteImages);

    int deletePostImagesByImageUrlIsIn(List<String> deleteImages);
    void deletePostImagesByIdIsIn(List<Long> ids);
}
