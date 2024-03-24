package com.example.courseWork.repositories.gameSavesRepositories;

import com.example.courseWork.models.gameModel.GameStateParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateParametersRepository extends JpaRepository<GameStateParameter,Integer> {
}
