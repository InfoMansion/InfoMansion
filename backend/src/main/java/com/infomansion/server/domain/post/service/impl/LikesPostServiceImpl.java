package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.domain.LikesPost;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.dto.LikesPostCreateRequestDto;
import com.infomansion.server.domain.post.repository.LikesPostRepository;
import com.infomansion.server.domain.post.repository.PostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikesPostServiceImpl implements LikesPostService {

    private final PostRepository postRepository;
    private final LikesPostRepository likesPostRepository;

    @Override
    public Long createLikesPost(LikesPostCreateRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return likesPostRepository.save(requestDto.toEntity(post)).getPost().getId();
    }

    @Override
    public void addLikes(Long postId) {
        LikesPost likesPost = likesPostRepository.findById(postId)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        likesPost.addPostLikes();
    }
}
