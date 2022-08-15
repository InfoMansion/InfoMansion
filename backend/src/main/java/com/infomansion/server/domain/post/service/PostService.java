package com.infomansion.server.domain.post.service;

import com.infomansion.server.domain.post.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Long createPost(PostCreateRequestDto requestDto);
    PostbyUserStuffResponseDto findPostByUserStuffId(Long userStuffId, Pageable pageable);
    PostSearchResponseDto findPostBySearchWordForTitle(String searchWord, Pageable pageable);
    PostSearchResponseDto findPostBySearchWordForContent(String searchWord, Pageable pageable);
    PostDetailResponseDto findPostWithUser(Long postId);
    List<PostSimpleResponseDto> findRecent5ByUser(String userName, Pageable pageable);
    boolean deletePost(Long postId);
    boolean modifyPostAndSaveAsTemp(PostModifyRequestDto requestDto);
    TempPostImageUploadResponseDto createTempPostAndUploadImage(MultipartFile multipartFile, TempPostSaveRequestDto requestDto);
    TempPostSaveResponseDto createTempPost(TempPostSaveRequestDto requestDto);
    PostSaveResponseDto createNewPost(PostSaveRequestDto requestDto);
    TempPostImageUploadResponseDto modifyPostAndImageUpload(MultipartFile multipartFile, TempPostSaveRequestDto requestDto, Long postId);
    TempPostSaveResponseDto modifyPostAndSaveAsTemp(TempPostSaveRequestDto requestDto, Long postId);
    PostSaveResponseDto publishPost(PostSaveRequestDto requestDto, Long postId);
    List<TempPostSaveResponseDto> findTempPosts();
    Slice<PostSimpleResponseDto> findPostInThePostbox(String username, Pageable pageable);
    Slice<PostGuestBookResponseDto> findPostInTheGuestbook(String username, Pageable pageable);
    Long createGuestBookPost(String username, PostGuestBookRequestDto requestDto);
    boolean modifyGuestBookPost(PostGuestBookModifyRequestDto requestDto);
}

