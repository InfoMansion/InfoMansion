package com.infomansion.server.domain.user.service;

public interface VerifyEmailService {
    void sendMail(String to, String sub, String text);
    void sendVerificationMail(String email);
    String verifyEmail(String key);
}
