package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;
import com.infomansion.server.domain.post.dto.PostSimpleResponseDto;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
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
    public PostRecommendResponseDto findRecommendPost() {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        String userCategories = user.getCategories();

        List<Category> categories = Arrays.stream(userCategories.split(",")).map(Category::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        //내 userID 제외
        List<Long> recommendPosts = postRepository.findTop13ByCategoryInAndModifiedDateBetween(user, categories, start, end);

        return new PostRecommendResponseDto(recommendPosts);
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

}
