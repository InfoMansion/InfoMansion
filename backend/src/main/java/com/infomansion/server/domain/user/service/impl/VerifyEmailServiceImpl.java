package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerifyEmailServiceImpl implements VerifyEmailService {

    private final String VERIFICATION_LINK = "http://localhost:8080/api/v1/auth/verification?key=";
    private final Long EXPIRATION_TIME = 60 * 30L;


    private final RedisUtil redisUtil;

    @Autowired
    private final JavaMailSender emailSender;


    @Override
    public void sendMail(String to, String sub, String text) {
        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("Info`Mansion <contactinfomansion@gmail.com>");
                helper.setTo(to);
                helper.setSubject(sub);
                helper.setText(text, true);
            }
        };

        emailSender.send(preparator);
    }

    @Override
    @Transactional
    public void sendVerificationMail(String email) {
        if(email == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        UUID uuid = UUID.randomUUID();

        redisUtil.setDataExpire(uuid.toString(), email, EXPIRATION_TIME);

        sendMail(email, "[Info`Mansion] 회원가입 이메일 인증", createVerificationEmail(VERIFICATION_LINK + uuid.toString()));
    }

    @Override
    @Transactional
    public String verifyEmail(String key) {
        String userEmail = redisUtil.getData(key);
        if(userEmail == null) throw new CustomException(ErrorCode.VERIFICATION_KEY_NOT_FOUND);

        redisUtil.deleteData(key);

        return userEmail;
    }

    private String createVerificationEmail(String verificationLink) {
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
                "                      src=\"http://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F41b10e70-976e-4ae1-8a54-882169e20920%2FvectorLogo.svg?table=block&id=7cceb917-5a16-4d61-8dd5-6a966317b21a&spaceId=2ad0c350-f7c4-49fc-8e10-0df09d3d599e&userId=f7809fb4-8b68-4ac4-940d-e69a299a85e6&cache=v2\"\n" +
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
                "                  InfoMansion에 오신 것을 환영합니다\n" +
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
                "                  아래 버튼을 클릭하여 가입을 완료해 주세요.\n" +
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
                "                        href=\""+ verificationLink + "\"\n" +
                "                          style=\"\n" +
                "                            font-family: sf pro text, roboto, segoe ui, helvetica neue, helvetica,\n" +
                "                              arial, sans-serif;\n" +
                "                            border: 1px solid #e8a0bf;\n" +
                "                            border-radius: 24px;\n" +
                "                            padding: 14.5px 16px;\n" +
                "                            display: inline-block;\n" +
                "                            font-size: 16px;\n" +
                "                            font-weight: bold;\n" +
                "                            line-: 17px;\n" +
                "                            color: #ffffff;\n" +
                "                            text-decoration: none;\n" +
                "                            box-sizing: border-box;\n" +
                "                          \"\n" +
                "                          rel=\"noreferrer noopener\"\n" +
                "                          target=\"_blank\"\n" +
                "                        >\n" +
                "                          가입 완료하기\n" +
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
}
