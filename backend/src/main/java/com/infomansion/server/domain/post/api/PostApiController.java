package com.infomansion.server.domain.post.api;

import com.infomansion.server.domain.post.dto.PostRequestDto;
import com.infomansion.server.domain.post.service.PostService;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<CommonResponse<Long>> createPost(@Valid @RequestBody PostRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createPost(requestDto)));
    }
}
