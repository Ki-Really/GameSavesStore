package com.example.courseWork.repositories.gameStateSharesRepositories;

import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.sharedSave.SharedSave;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameStateSharesRepository extends JpaRepository<SharedSave,Integer>{
    List<SharedSave> findByGameState(GameState gameState);
}
