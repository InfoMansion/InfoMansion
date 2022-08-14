package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.notification.domain.Notification;
import com.infomansion.server.domain.notification.domain.NotificationType;
import com.infomansion.server.domain.notification.repository.NotificationRepository;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.domain.UserLikePost;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.repository.UserLikePostRepository;
import com.infomansion.server.domain.post.service.UserLikePostService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserLikePostServiceImpl implements UserLikePostService {

    private final UserLikePostRepository userLikePostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    @Override
    @Transactional
    public Long likePost(Long postId) {
        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(userLikePostRepository.existsUserLikePostByPostAndUser(post, user))
               throw new CustomException(ErrorCode.POST_ALREADY_LIKES);

        UserLikePost ulp = UserLikePost.likePost(post, user);
        userLikePostRepository.save(ulp);
        post.addUserLikePost(ulp);
        notificationRepository.save(Notification.createNotification(user.getUsername(), post.getUser().getUsername(), NotificationType.LIKE_POST, postId));
        return postId;
    }

    @Override
    @Transactional
    public Long unlikePost(Long postId) {
        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserLikePost ulp = userLikePostRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_LIKES_NOT_FOUND));

        userLikePostRepository.delete(ulp);
        post.removeUserLikePost(ulp);
        return postId;
    }

    @Override
    public List<UserSimpleProfileResponseDto> findUsersLikeThisPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<UserLikePost> usersLikeThisPost = userLikePostRepository.findUsersLikeThisPost(post);

        if(usersLikeThisPost.isEmpty()) {
            return null;
        }

        return usersLikeThisPost.stream().map(userLikePost -> UserSimpleProfileResponseDto.toDto(userLikePost.getUser())).collect(Collectors.toList());
    }

    public Slice<PostSimpleResponseDto> findPostsUserLikes(Pageable pageable) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return userLikePostRepository.findPostsByUserLike(user, pageable)
                .map(userLikePost -> new PostSimpleResponseDto(userLikePost.getPost()));
    }
}
