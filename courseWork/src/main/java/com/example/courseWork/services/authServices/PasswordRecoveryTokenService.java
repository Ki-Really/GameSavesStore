package com.example.courseWork.services.authServices;

import com.example.courseWork.models.authModel.PasswordRecoveryTokenEntity;
import com.example.courseWork.repositories.authRepositories.PasswordRecoveryTokenRepository;
import com.example.courseWork.util.exceptions.personException.InvalidTokenException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PasswordRecoveryTokenService {

    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;

    @Autowired
    public PasswordRecoveryTokenService(PasswordRecoveryTokenRepository passwordRecoveryTokenRepository) {
        this.passwordRecoveryTokenRepository = passwordRecoveryTokenRepository;
    }

    public PasswordRecoveryTokenEntity findByToken(String token){
        Optional<PasswordRecoveryTokenEntity> personToChangePassword = passwordRecoveryTokenRepository.findByToken(token);
        return personToChangePassword.orElseThrow(() -> new InvalidTokenException("Token is invalid or not exists:" + token));
    }

    @Transactional
    public void save(PasswordRecoveryTokenEntity passwordRecoveryTokenEntity){
        passwordRecoveryTokenRepository.save(passwordRecoveryTokenEntity);
    }

    @Transactional
    public void remove(PasswordRecoveryTokenEntity passwordRecoveryTokenEntity){
        passwordRecoveryTokenRepository.deleteById(passwordRecoveryTokenEntity.getId());
    }

    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        passwordRecoveryTokenRepository.deleteByExpiryDateBefore(now);
    }
}
