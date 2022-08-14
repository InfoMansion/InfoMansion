package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface UserLikePostService {
    Long likePost(Long postId);
    Long unlikePost(Long postId);
    List<UserSimpleProfileResponseDto> findUsersLikeThisPost(Long postId);
    Slice<PostSimpleResponseDto> findPostsUserLikes(Pageable pageable);
}
