package com.infomansion.server.domain.upload.api;

import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3ApiController {

    private final S3Uploader s3Uploader;

    @PostMapping("/api/v1/image/profile/{userId}")
    public ResponseEntity<? extends BasicResponse> updateUserImage(@RequestParam("images")MultipartFile multipartFile, @PathVariable("userId") Long userId) {
        try {
            s3Uploader.uploadFiles(multipartFile, "static");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/v1/image/post")
    public ResponseEntity<? extends BasicResponse> uploadImageForPost(@RequestParam("image") MultipartFile multipartFile) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse<>(s3Uploader.uploadFiles(multipartFile, "post")));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
