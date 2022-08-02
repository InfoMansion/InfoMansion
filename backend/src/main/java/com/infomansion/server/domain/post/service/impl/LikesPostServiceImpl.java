package com.infomansion.server.domain.post.service.impl;

import com.infomansion.server.domain.post.domain.LikesPost;
import com.infomansion.server.domain.post.repository.LikesPostRepository;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikesPostServiceImpl implements LikesPostService {

    private final LikesPostRepository likesPostRepository;

    @Transactional
    @Override
    public void addLikes(Long postId) {
        LikesPost likesPost = likesPostRepository.findById(postId)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        likesPost.addPostLikes();
    }
}
