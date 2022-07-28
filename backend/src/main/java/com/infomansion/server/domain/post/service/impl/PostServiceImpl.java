package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.LikesPostCreateRequestDto;
import com.infomansion.server.domain.post.dto.PostCreateRequestDto;
import com.infomansion.server.domain.post.repository.LikesPostRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.stuff.repository.StuffRepository;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import com.infomansion.server.domain.userstuff.repository.UserStuffRepository;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final UserStuffRepository userStuffRepository;
    private final PostRepository postRepository;

    private final LikesPostService likesPostService;

    @Transactional
    @Override
    public Long createPost(PostCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserStuff userStuff = userStuffRepository.findById(requestDto.getUserStuffId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STUFF_NOT_FOUND));

        Long postId = postRepository.save(requestDto.toEntity(user, userStuff)).getId();

        LikesPostCreateRequestDto likeCreaterequestDto= LikesPostCreateRequestDto.builder()
                .postId(postId).build();
        return likesPostService.createLikesPost(likeCreaterequestDto);
    }

}
