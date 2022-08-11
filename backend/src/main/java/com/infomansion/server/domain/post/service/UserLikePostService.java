package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;

import java.util.List;

public interface UserLikePostService {
    Long likePost(Long postId);
    Long unlikePost(Long postId);
    List<UserSimpleProfileResponseDto> findUsersLikeThisPost(Long postId);
    List<PostSimpleResponseDto> findPostsUserLikes();
}
