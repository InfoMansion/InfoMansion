package com.infomansion.server.domain.user.api;

import com.infomansion.server.domain.user.auth.AccessTokenRequestDto;
import com.infomansion.server.domain.user.auth.AccessTokenResponseDto;
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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final UserService userService;

    @Value("${spring.mail.redirectURI}")
    private String redirectURI;

    @Value("${infomansion.cookie.domain}")
    private String cookieDomain;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<CommonResponse<Long>> userSignUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(userService.join(requestDto)));
    }

    @GetMapping("/api/v1/auth/verification")
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
    public ResponseEntity<? extends BasicResponse> userLogin(@Valid @RequestBody UserLoginRequestDto requestDto) {

        TokenDto tokenDto = userService.login(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, createAccessTokenCookie(tokenDto).toString())
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(tokenDto).toString())
                .body(new CommonResponse<>(new AccessTokenResponseDto(tokenDto)));
    }

    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<? extends BasicResponse> userReissue(@Valid @RequestBody AccessTokenRequestDto requestDto, @CookieValue(name = "InfoMansionRefreshToken") String refreshToken) {
        TokenDto tokenDto = userService.reissue(new ReissueDto(requestDto.getAccessToken(), refreshToken));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, createAccessTokenCookie(tokenDto).toString())
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(tokenDto).toString())
                .body(new CommonResponse<>(new AccessTokenResponseDto(tokenDto)));
    }

    @GetMapping("/api/v1/auth/logout")
    public ResponseEntity<? extends  BasicResponse> userLogout() {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, removeAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, removeRefreshTokenCookie().toString())
                .body(new CommonResponse<>(userService.logout()));
    }

    private ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from("InfoMansionRefreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/")
                .maxAge(0L)
                .sameSite("Strict")
                .domain(cookieDomain)
                .build();
    }

    private ResponseCookie removeAccessTokenCookie() {
        return ResponseCookie.from("InfoMansionAccessToken", "")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .domain(cookieDomain)
                .build();
    }

    private ResponseCookie createRefreshTokenCookie(TokenDto tokenDto) {
        return ResponseCookie.from("InfoMansionRefreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/")
                .maxAge(Duration.ofMillis(tokenDto.getRefreshTokenExpiresTime()))
                .sameSite("Strict")
                .domain(cookieDomain)
                .build();
    }

    private ResponseCookie createAccessTokenCookie(TokenDto tokenDto) {
        return ResponseCookie.from("InfoMansionAccessToken", tokenDto.getAccessToken())
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(1000*60*30L))
                .sameSite("Strict")
                .domain(cookieDomain)
                .build();
    }
}
