package com.example.courseWork.repositories.gameSavesRepositories;

import com.example.courseWork.models.gameSaveModel.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameStatesRepository extends JpaRepository<GameState, Integer> {
    Optional<GameState> findByName(String name);
    Optional<GameState> findById(int id);
    void deleteById(int id);
}
