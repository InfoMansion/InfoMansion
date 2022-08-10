package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.repository.UserLikePostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.FollowRepository;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final UserStuffRepository userStuffRepository;
    private final PostRepository postRepository;
    private final UserLikePostRepository userLikePostRepository;
    private final FollowRepository followRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    @Override
    public Long createPost(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        List<String> deleteImages = requestDto.getImages()
                .stream().filter(image -> !requestDto.getContent().contains(image))
                .collect(Collectors.toList());
        if (deleteImages.size() > 0)
            s3Uploader.deleteFiles(deleteImages);

        return postRepository.save(requestDto.toEntity(user, userStuff)).getId();
    }


    @Override
    public List<Long> findRecommendPost() {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String userCategories = user.getCategories();

        List<Category> categories = Arrays.stream(userCategories.split(",")).map(Category::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        List<Long> recommendUserIds = postRepository.findTop27ByCategoryInAndModifiedDateBetween(user, categories, start, end);

        if(recommendUserIds.size()>27){
            return new ArrayList<>(recommendUserIds.subList(0,27));
        }
        return recommendUserIds;
    }

    @Override
    public List<Long> findRecommendPostByUserLikePost() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String userCategories = user.getCategories();

        List<Category> categories = Arrays.stream(userCategories.split(",")).map(Category::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        List<Long> recommendUserIds = postRepository.findTop27PostByUserLikePost(user, categories, start, end);

        if(recommendUserIds.size()>27){
            return new ArrayList<>(recommendUserIds.subList(0,27));
        }
        return recommendUserIds;
    }

    @Override
    public List<PostSimpleResponseDto> findPostByUserStuffId(Long userStuffId) {

        UserStuff userStuff = userStuffRepository.findById(userStuffId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        List<PostSimpleResponseDto> simpleResponseDtos = new ArrayList<>();
        postRepository.findByUserStuffId(userStuff)
                .forEach(post -> simpleResponseDtos.add(new PostSimpleResponseDto(post)));

        return simpleResponseDtos;
    }

    @Override
    public PostSearchResponseDto findPostBySearchWordForTitle(String searchWord, Pageable pageable) {
        Slice<PostSimpleResponseDto> postsByTitle =
                postRepository.findByTitle(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);
        return new PostSearchResponseDto(postsByTitle);
    }

    @Override
    public PostSearchResponseDto findPostBySearchWordForContent(String searchWord, Pageable pageable) {
        Slice<PostSimpleResponseDto> postsByTitle =
                postRepository.findByContent(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);
        return new PostSearchResponseDto(postsByTitle);
    }

    @Override
    public PostDetailResponseDto findPostWithUser(Long postId) {

        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostDetailResponseDto.toDto(post, followRepository.existsByFromUserIdAndToUserIs(SecurityUtil.getCurrentUserId(), post.getUser()));
    }

    @Override
    public List<PostSimpleResponseDto> findRecent5ByUser(String userName, Pageable pageable) {
        int size = 5;
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();

        if(!user.getId().equals(SecurityUtil.getCurrentUserId()) && user.isPrivateFlag()){
            throw new CustomException(ErrorCode.NOT_PUBLIC_USER);
        }
        List<PostSimpleResponseDto> top5RecentPost = new ArrayList<>();
        postRepository.findTop5Post(userId, PageRequest.of(0,size))
                .forEach(post -> top5RecentPost.add(new PostSimpleResponseDto(post)));

        return top5RecentPost;
    }

    @Transactional
    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.getUser().getId() != SecurityUtil.getCurrentUserId() && post.getUserStuff().getUser().getId() != SecurityUtil.getCurrentUserId())
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);

        post.deletePost();
        return true;
    }

    @Override
    public boolean modifyPost(PostModifyRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(post.getUserStuff().getId() != requestDto.getUserStuffId()) {
            UserStuff userStuffToMove = userStuffRepository.findById(requestDto.getUserStuffId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));
            post.updatePostWithUserStuff(userStuffToMove, requestDto.getTitle(), requestDto.getContent());
        } else
            post.updatePost(requestDto.getTitle(), requestDto.getContent());

        List<String> deleteImages = requestDto.getImages()
                .stream().filter(image -> !requestDto.getContent().contains(image))
                .collect(Collectors.toList());
        if (deleteImages.size() > 0)
            s3Uploader.deleteFiles(deleteImages);

        return true;
    }

}
