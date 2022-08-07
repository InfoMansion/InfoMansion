package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.Room.domain.Room;
import com.infomansion.server.domain.Room.repository.RoomRepository;
import com.infomansion.server.domain.upload.service.S3Uploader;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.*;
import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.UserService;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.domain.userstuff.service.UserStuffService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.jwt.ReissueDto;
import com.infomansion.server.global.util.jwt.TokenDto;
import com.infomansion.server.global.util.jwt.TokenProvider;
import com.infomansion.server.global.util.redis.RedisUtil;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.infomansion.server.domain.category.util.CategoryUtil.validateCategories;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final VerifyEmailService verifyEmailService;
    private final S3Uploader s3Uploader;
    private final UserStuffService userStuffService;
    private final RoomRepository roomRepository;


    @Override
    @Transactional
    public Long join(UserSignUpRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        validateCategories(requestDto.getCategories());
        verifyEmailService.sendVerificationMail(requestDto.getEmail());
        return userRepository.save(requestDto.toEntityWithEncryptPassword(passwordEncoder)).getId();
    }

    @Override
    @Transactional
    public TokenDto login(UserLoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisUtil.setDataExpire("RT:"+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Override
    @Transactional
    public TokenDto reissue(ReissueDto reissueDto) {
        if(!tokenProvider.validateToken(reissueDto.getAccessToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(reissueDto.getAccessToken());

        String refreshToken = redisUtil.getData("RT:" + authentication.getName());

        if(!refreshToken.equals(reissueDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisUtil.setDataExpire("RT:"+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Override
    public UserModifyProfileResponseDto authBeforeChangePassword(UserAuthRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        return UserModifyProfileResponseDto.toDto(user);
    }

    @Override
    @Transactional
    public Long changePasswordAfterAuth(UserChangePasswordDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return user.getId();
    }

    @Override
    @Transactional
    public boolean verifiedByEmail(String key) {
        String email = verifyEmailService.verifyEmail(key);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.grantFromTempToUser();
        roomRepository.save(Room.createRoom(user));
        userStuffService.saveDefaultUserStuff(user);
        return true;
    }

    @Override
    @Transactional
    public boolean logout() {
        String data = redisUtil.getData("RT:" + SecurityUtil.getCurrentUserId());
        if(data == null) {
            // 유효하지 않은 accessToken을 통한 로그아웃 요청
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
        redisUtil.deleteData("RT:"+SecurityUtil.getCurrentUserId());
        return true;
    }

    @Override
    public UserInfoResponseDto findByUsername(String username) {
        return UserInfoResponseDto.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)), SecurityUtil.getCurrentUserId());
    }

    @Override
    public UserSimpleProfileResponseDto findSimpleProfile() {
        return UserSimpleProfileResponseDto.toDto(userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public UserModifyProfileResponseDto modifyUserProfile(MultipartFile profileImage, UserModifyProfileRequestDto profileInfo) {
        if (userRepository.existsByUsername(profileInfo.getUsername()))
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);

        validateCategories(profileInfo.getCategories());

        User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        try {
            user.changeProfileImage(s3Uploader, profileImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.modifyProfile(profileInfo);
        return UserModifyProfileResponseDto.toDto(user);
    }

    @Override
    @Transactional
    public boolean resetPassword(UserResetPasswordRequestDto requestDto, String redirectURL) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!user.getUsername().equals(requestDto.getUsername())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        String tempPassword = getRandomPassword((int) (Math.random() * 13) + 8);
        user.changePassword(passwordEncoder.encode(tempPassword));

        verifyEmailService.sendMail(user.getEmail(), "[Info`Mansion] 임시 비밀번호 발급", createResetPasswordEmail(tempPassword, redirectURL));

        return true;
    }

    @Override
    public UserSearchResponseDto findUserBySearchWordForUserName(String searchWord, Pageable pageable) {
        Slice<UserSimpleProfileResponseDto> usersByUserName =
                userRepository.findUserByUserName(searchWord, pageable)
                        .map(UserSimpleProfileResponseDto::toDto);
        return new UserSearchResponseDto(usersByUserName);
    }

    private String getRandomPassword(int size) {
        final String regex = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}";

        char[] charSet = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '&'
        };
        StringBuilder sb = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        while (true) {
            for (int i = 0; i < size; i++) {
                int idx = sr.nextInt(charSet.length);
                sb.append(charSet[idx]);
            }

            if (Pattern.matches(regex, sb.toString())) {
                return sb.toString();
            }
        }
    }

    private String createResetPasswordEmail(String tempPassword, String redirectURL) {
        return "<div id=\"readFrame\">\n" +
                "      <table\n" +
                "        align=\"center\"\n" +
                "        border=\"0\"\n" +
                "        cellpadding=\"0\"\n" +
                "        cellspacing=\"0\"\n" +
                "        style=\"border-collapse: collapse\"\n" +
                "        width=\"100%\"\n" +
                "      >\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\">\n" +
                "              <div>\n" +
                "                <div style=\"margin-bottom: 8px\">\n" +
                "                    <img\n" +
                "                      border=\"0\"\n" +
                "                      height=\"60\"\n" +
                "                      src=\"https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F41b10e70-976e-4ae1-8a54-882169e20920%2FvectorLogo.svg?table=block&id=7cceb917-5a16-4d61-8dd5-6a966317b21a&spaceId=2ad0c350-f7c4-49fc-8e10-0df09d3d599e&userId=f7809fb4-8b68-4ac4-940d-e69a299a85e6&cache=v2\"\n" +
                "                      style=\"\n" +
                "                        display: inline-block;\n" +
                "                        width: 60;\n" +
                "                        height: 60;\n" +
                "                        outline: none;\n" +
                "                        border: 0;\n" +
                "                        -ms-interpolation-mode: bicubic;\n" +
                "                      \"\n" +
                "                      width=\"60\"\n" +
                "                      loading=\"lazy\"\n" +
                "                    />\n" +
                "                  </a>\n" +
                "                </div>\n" +
                "                <span\n" +
                "                  style=\"\n" +
                "                    display: inline-block;\n" +
                "                    padding: 0 8px;\n" +
                "                    font-family: SF Pro Text, Roboto, Segoe UI, helvetica neue, helvetica, arial,\n" +
                "                      sans-serif;\n" +
                "                    font-size: 20px;\n" +
                "                    line-height: 23px;\n" +
                "                    font-weight: 700;\n" +
                "                    color: #111111;\n" +
                "                    text-decoration: none;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  Info`Mansion\n" +
                "                </span>\n" +
                "              </div>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "      <table\n" +
                "        align=\"center\"\n" +
                "        border=\"0\"\n" +
                "        cellpadding=\"0\"\n" +
                "        cellspacing=\"0\"\n" +
                "        style=\"border-collapse: collapse\"\n" +
                "        width=\"100%\"\n" +
                "      >\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td>\n" +
                "              <div style=\"height: 16px; font-size: 16px; line-height: 16px; display: none\">\n" +
                "                &nbsp;\n" +
                "              </div>\n" +
                "              <!--[if !mso]><!-- -->\n" +
                "              <div style=\"height: 8px; font-size: 8px; line-height: 8px\">&nbsp;</div>\n" +
                "              <!--&lt;![endif]-->\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "      <table\n" +
                "        align=\"center\"\n" +
                "        border=\"0\"\n" +
                "        cellpadding=\"0\"\n" +
                "        cellspacing=\"0\"\n" +
                "        style=\"border-collapse: collapse\"\n" +
                "        width=\"100%\"\n" +
                "      >\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\">\n" +
                "              <div style=\"text-align: center\">\n" +
                "                <span\n" +
                "                  style=\"\n" +
                "                    display: inline-block;\n" +
                "                    font-size: 16px;\n" +
                "                    line-height: 19px;\n" +
                "                    padding: 0 8px;\n" +
                "                    font-family: SF Pro Text, Roboto, Segoe UI, helvetica neue, helvetica, arial,\n" +
                "                      sans-serif;\n" +
                "                    color: #111111;\n" +
                "                    text-align: center;\n" +
                "                    text-decoration: none;\n" +
                "                    font-weight: normal;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  새로 발급된 비밀번호는 : "+tempPassword+" 입니다.\n" +
                "                  <br />\n" +
                "                  아래 버튼을 통해 로그인 후 비밀번호를 변경해주세요.\n" +
                "                  <br />\n" +
                "                </span>\n" +
                "              </div>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "      <table\n" +
                "        align=\"center\"\n" +
                "        border=\"0\"\n" +
                "        cellpadding=\"0\"\n" +
                "        cellspacing=\"0\"\n" +
                "        style=\"border-collapse: collapse\"\n" +
                "        width=\"100%\"\n" +
                "      >\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td>\n" +
                "              <div style=\"height: 32px; font-size: 32px; line-height: 32px\">&nbsp;</div>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "      <table\n" +
                "        align=\"center\"\n" +
                "        border=\"0\"\n" +
                "        cellpadding=\"0\"\n" +
                "        cellspacing=\"0\"\n" +
                "        style=\"border-collapse: collapse\"\n" +
                "        width=\"100%\"\n" +
                "      >\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\">\n" +
                "              <div>\n" +
                "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                  <tbody>\n" +
                "                    <tr>\n" +
                "                      <td align=\"center\" valign=\"top\" bgcolor=\"#fc7a71\" style=\"border-radius: 24px\">\n" +
                "                        <a\n" +
                "                          href=\""+ redirectURL +"\"\n" +
                "                          style=\"\n" +
                "                            font-family: sf pro text, roboto, segoe ui, helvetica neue, helvetica,\n" +
                "                              arial, sans-serif;\n" +
                "                            border: 1px solid #e8a0bf;\n" +
                "                            border-radius: 24px;\n" +
                "                            padding: 14.5px 16px;\n" +
                "                            display: inline-block;\n" +
                "                            font-size: 16px;\n" +
                "                            font-weight: bold;\n" +
                "                            line-height: 17px;\n" +
                "                            color: #ffffff;\n" +
                "                            text-decoration: none;\n" +
                "                            box-sizing: border-box;\n" +
                "                          \"\n" +
                "                          rel=\"noreferrer noopener\"\n" +
                "                          target=\"_blank\"\n" +
                "                        >\n" +
                "                          InfoMansion으로 이동\n" +
                "                        </a>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </tbody>\n" +
                "                </table>\n" +
                "              </div>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </div>";
    }

    private void validateDuplicateUser(UserSignUpRequestDto requestDto) {
        // email 중복 검증
        if(userRepository.existsByEmail(requestDto.getEmail())) throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);

        // username 중복 검증
        if(userRepository.existsByUsername(requestDto.getUsername())) throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }
}
