package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostDetailResponseDto;
import com.infomansion.server.domain.post.dto.PostSearchResponseDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    @Override
    public Long createPost(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

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

        List<Long> recommendUserIds = postRepository.findTop13ByCategoryInAndModifiedDateBetween(user, categories, start, end);

        if(recommendUserIds.size()>13){
            List<Long> subRecommendUserIds = new ArrayList<>(recommendUserIds.subList(0,13));
            return subRecommendUserIds;
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
    public PostSearchResponseDto findPostBySearchWord(String searchWord, Pageable pageable) {

        Slice<UserSimpleProfileResponseDto> usersByUserName =
                userRepository.findUserByUserName(searchWord, pageable)
                        .map(UserSimpleProfileResponseDto::toDto);

        Slice<PostSimpleResponseDto> postsByTitle =
                postRepository.findByTitle(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);

        Slice<PostSimpleResponseDto> postsByContent =
                postRepository.findByContent(searchWord, pageable)
                        .map(PostSimpleResponseDto::new);

        return new PostSearchResponseDto(usersByUserName, postsByTitle, postsByContent);
    }

    @Override
    public PostDetailResponseDto findPostWithUser(Long postId) {

        Post post = postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return new PostDetailResponseDto(post);
    }

}
