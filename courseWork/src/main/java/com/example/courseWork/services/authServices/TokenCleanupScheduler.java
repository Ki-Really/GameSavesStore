package com.example.courseWork.services.authServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupScheduler {

    private final PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Autowired
    public TokenCleanupScheduler(PasswordRecoveryTokenService passwordRecoveryTokenService) {
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
    }
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredTokens() {
        passwordRecoveryTokenService.deleteExpiredTokens();
    }
}
