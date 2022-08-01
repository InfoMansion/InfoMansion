package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.LikesPost;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostRecommendResponseDto;
import com.infomansion.server.domain.post.repository.LikesPostRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
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

import java.time.LocalDate;
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
    private final LikesPostRepository likesPostRepository;
    private final LikesPostService likesPostService;

    @Transactional
    @Override
    public Long createPost(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        Long postId = postRepository.save(requestDto.toEntity(user, userStuff)).getId();
        Post post = postRepository.findById(postId).get();

        likesPostRepository.save(LikesPost.builder().post(post).build());
        return postId;
    }

    @Override
    public List<PostRecommendResponseDto> findRecommendPost(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        String userCategories = user.getCategories();

        List<Category> categories = Arrays.asList(userCategories.split(","))
                .stream().map(Category::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDate.now().atStartOfDay();

        List<PostRecommendResponseDto> recommendPosts = new ArrayList<>();

        //내 userID 제외
        postRepository.findTop13ByCategoryInAndModifiedDateBetween(userId, categories, start, end)
                .forEach(rPost -> recommendPosts.add(new PostRecommendResponseDto(rPost)));

        return recommendPosts;
    }

}
