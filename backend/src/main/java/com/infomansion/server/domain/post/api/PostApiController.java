package com.infomansion.server.domain.post.api;

import com.infomansion.server.domain.post.dto.*;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.domain.post.service.UserLikePostService;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;
    private final UserLikePostService userLikePostService;
    private final S3Uploader s3Uploader;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<CommonResponse<Long>> createPost(@Valid @RequestBody PostCreateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createPost(requestDto)));
    }

    @GetMapping("api/v1/posts/{userStuffId}")
    public ResponseEntity<CommonResponse<PostbyUserStuffResponseDto>> findPostByUserStuff(@Valid @PathVariable Long userStuffId, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostByUserStuffId(userStuffId, pageable)));
    }

    @GetMapping("/api/v1/posts/postbox/{username}")
    public ResponseEntity<? extends BasicResponse> findPostInThePostbox(@Valid @PathVariable String username, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostInThePostbox(username, pageable)));
    }

    @GetMapping("/api/v1/posts/guestbook/{username}")
    public ResponseEntity<? extends BasicResponse> findPostInTheGuestbook(@Valid @PathVariable String username, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findPostInTheGuestbook(username, pageable)));
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

    @DeleteMapping("/api/v2/posts/likes/{postId}")
    public ResponseEntity<? extends BasicResponse> userUnlikesPost(@Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userLikePostService.unlikePost(postId)));
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
    public ResponseEntity<? extends BasicResponse> modifyPostAndSaveAsTemp(@Valid @RequestBody PostModifyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.modifyPostAndSaveAsTemp(requestDto)));
    }

    @PostMapping("/api/v1/posts/reset")
    public ResponseEntity<? extends BasicResponse> removeUploadImages(@Valid @RequestBody List<String> deleteImages) {
        s3Uploader.deleteFiles(deleteImages);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(true));
    }

    /**
     * 새로 작성하는 포스트를 임시 저장하면서 이미지를 업로드
     * @param multipartFile
     * @param requestDto
     * @return
     */
    @PostMapping(value = "/api/v2/posts/upload/temp", consumes = {"multipart/form-data"})
    public ResponseEntity<? extends BasicResponse> createTempPostAndUploadImage(@Valid @RequestPart("image") MultipartFile multipartFile,
                                                                                @Valid @RequestPart("post") TempPostSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createTempPostAndUploadImage(multipartFile, requestDto)));
    }

    /**
     * 새로 작성하는 포스트를 임시 저장
     * @param requestDto
     * @return
     */
    @PostMapping("/api/v2/posts/temp")
    public ResponseEntity<? extends BasicResponse> createTempPost(@Valid @RequestBody TempPostSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createTempPost(requestDto)));
    }

    /**
     * 새로 작성하는 포스트를 발행
     * @param requestDto
     * @return
     */
    @PostMapping("/api/v2/posts")
    public ResponseEntity<? extends BasicResponse> createNewPost(@Valid @RequestBody PostSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createNewPost(requestDto)));
    }

    /**
     * 임시 포스트 또는 발행된 포스트에 이미지 업로드
     * @param multipartFile
     * @param requestDto
     * @param postId
     * @return
     */
    @PostMapping(value = "/api/v2/posts/upload/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<? extends BasicResponse> modifyPostAndImageUpload(@Valid @RequestPart(value = "image") MultipartFile multipartFile,
                                                                                @Valid @RequestPart("post") TempPostSaveRequestDto requestDto,
                                                                                @Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.modifyPostAndImageUpload(multipartFile, requestDto, postId)));
    }

    /**
     * 임시 포스트 또는 발행된 포스트를 수정하고 임시 포스트로 저장
     * @param requestDto
     * @param postId
     * @return
     */
    @PostMapping("/api/v2/posts/temp/{postId}")
    public ResponseEntity<? extends BasicResponse> modifyPostAndSaveAsTemp(@Valid @RequestBody TempPostSaveRequestDto requestDto, @Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.modifyPostAndSaveAsTemp(requestDto, postId)));
    }

    /**
     * 임시 포스트를 발행하거나, 발행된 포스트를 재 발행
     * @param requestDto
     * @param postId
     * @return
     */
    @PostMapping("/api/v2/posts/{postId}")
    public ResponseEntity<? extends BasicResponse> publishPost(@Valid @RequestBody PostSaveRequestDto requestDto, @Valid @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.publishPost(requestDto, postId)));
    }

    /**
     * 임시 포스트 목록을 조회
     */
    @GetMapping("/api/v2/posts/temp")
    public ResponseEntity<? extends BasicResponse> findTempPosts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.findTempPosts()));
    }
}
