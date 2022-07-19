package com.infomansion.server.domain.user.service.impl;

import com.infomansion.server.domain.user.repository.UserRepository;
import com.infomansion.server.domain.user.service.VerifyEmailService;
import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import com.infomansion.server.global.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class VerifyEmailServiceImpl implements VerifyEmailService {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Autowired
    private JavaMailSender emailSender;


    @Override
    public void sendMail(String to, String sub, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendVerificationMail(String email) {
        String VERIFICATION_LINK = "http://localhost:8080/api/v1/users/verify/";

        if(email == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        UUID uuid = UUID.randomUUID();

        redisUtil.setDataExpire(uuid.toString(), email, 60 * 30L);

        sendMail(email, "[Info`Mansion] 회원가입 이메일 인증", VERIFICATION_LINK + uuid.toString());
    }

    @Override
    public void verifyEmail(String key) {
        String userEmail = redisUtil.getData(key);
        if(userEmail == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        redisUtil.deleteData(key);
    }
}
