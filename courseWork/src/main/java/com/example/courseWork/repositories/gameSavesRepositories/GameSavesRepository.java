package com.example.courseWork.repositories.gameSavesRepositories;

import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameSaveModel.GameSave;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSavesRepository extends JpaRepository<GameSave, Integer> {
    Optional<GameSave> findByName(String name);
    Optional<GameSave> findById(int id);
}
