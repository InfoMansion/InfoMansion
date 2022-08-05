package com.infomansion.server.domain.post.api;

import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;
    private final LikesPostService likesPostService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<CommonResponse<Long>> createPost(@Valid @RequestBody PostCreateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createPost(requestDto)));
    }

    @GetMapping("api/v1/posts/recommend")
    public ResponseEntity<CommonResponse<PostRecommendResponseDto>> findRecommendPostUser() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findRecommendPost()));
    }

    @GetMapping("api/v1/posts/{userStuffId}")
    public ResponseEntity<CommonResponse<List<PostSimpleResponseDto>>> findPostByUserStuff(@Valid @PathVariable Long userStuffId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostByUserStuffId(userStuffId)));
    }

    @PutMapping("api/v1/posts/likes/{postId}")
    public ResponseEntity<CommonResponse<Long>> addPostLikes(@Valid @PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(likesPostService.addLikes(postId)));
    }

    @GetMapping("api/v1/posts/search/{searchWord}")
    public ResponseEntity<CommonResponse<PostSearchResponseDto>> searchPostByWord(@Valid @PathVariable String searchWord, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostBySearchWord(searchWord, pageable)));
    }

    @GetMapping("api/v1/posts/detail/{postId}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> findDetailPost(@Valid @PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostWithUser(postId)));
    }
}
