package com.infomansion.server.domain.post.api;

import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.service.LikesPostService;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.post.service.UserLikePostService;
import com.infomansion.server.global.apispec.BasicResponse;
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
    private final UserLikePostService userLikePostService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<CommonResponse<Long>> createPost(@Valid @RequestBody PostCreateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createPost(requestDto)));
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

    @GetMapping("api/v1/posts/search/title")
    public ResponseEntity<CommonResponse<PostSearchResponseDto>> searchPostByTitle(@Valid @RequestParam String searchWord, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostBySearchWordForTitle(searchWord, pageable)));
    }

    @GetMapping("api/v1/posts/search/content")
    public ResponseEntity<CommonResponse<PostSearchResponseDto>> searchPostByContent(@Valid @RequestParam String searchWord, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostBySearchWordForContent(searchWord, pageable)));
    }

    @GetMapping("api/v1/posts/detail/{postId}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> findDetailPost(@Valid @PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostWithUser(postId)));
    }

    @GetMapping("api/v1/posts/recent")
    public ResponseEntity<CommonResponse<List<PostSimpleResponseDto>>> findRecentPosts(@Valid @RequestParam String userName, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findRecent5ByUser(userName, pageable)));
    }


    @PostMapping("/api/v2/posts/likes/{postId}")
    public ResponseEntity<? extends BasicResponse> userLikesPost(@Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userLikePostService.likePost(postId)));
    }

    @GetMapping("/api/v2/posts/likes/{postId}")
    public ResponseEntity<? extends BasicResponse> getLikesOnPost(@Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userLikePostService.findUsersLikeThisPost(postId)));
    }

    @GetMapping("/api/v2/posts/my-likes")
    public ResponseEntity<? extends BasicResponse> getPostsUserLikes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userLikePostService.findPostsUserLikes()));
    }

    @PatchMapping("/api/v1/posts/{postId}")
    public ResponseEntity<? extends BasicResponse> deletePost(@Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.deletePost(postId)));
    }

    @PutMapping("/api/v1/posts")
    public ResponseEntity<? extends BasicResponse> modifyPost(@Valid @RequestBody PostModifyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.modifyPost(requestDto)));
    }
}
