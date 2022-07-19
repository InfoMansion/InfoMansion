package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.dto.UserLoginRequestDto;
import com.infomansion.server.domain.user.dto.UserSignUpRequestDto;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final UserService userService;

    @Value("${spring.mail.redirectURI}")
    private String redirectURI;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<CommonResponse<Long>> userSignUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userService.join(requestDto)));
    }

    @GetMapping("/api/v1/auth/verify")
    public ResponseEntity<? extends BasicResponse> userVerifyEmail(@Valid @RequestParam String key) {
        try {
            URI redirectUri = new URI(redirectURI);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);

            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .headers(httpHeaders)
                    .body(new CommonResponse<>(userService.verifiedByEmail(key)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }


    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<CommonResponse<TokenDto>> userLogin(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.login(requestDto)));
    }

    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<? extends BasicResponse> userReissue(@Valid @RequestBody ReissueDto reissueDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.reissue(reissueDto)));
    }
}
