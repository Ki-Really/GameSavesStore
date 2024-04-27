package com.example.courseWork.repositories.authRepositories;

import com.example.courseWork.models.authModel.PasswordRecoveryTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryTokenEntity,Integer> {
    Optional<PasswordRecoveryTokenEntity> findByToken(String token);
    void deleteByExpiryDateBefore(LocalDateTime now);
}
