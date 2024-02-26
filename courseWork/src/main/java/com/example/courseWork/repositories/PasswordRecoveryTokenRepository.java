package com.example.courseWork.repositories;

import com.example.courseWork.models.PasswordRecoveryTokenEntity;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryTokenEntity,Integer> {
    Optional<PasswordRecoveryTokenEntity> findByToken(String token);
}
