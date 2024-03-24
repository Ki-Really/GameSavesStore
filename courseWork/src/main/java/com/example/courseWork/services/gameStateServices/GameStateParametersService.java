package com.example.courseWork.services.gameStateServices;

import com.example.courseWork.models.gameModel.GameStateParameter;
import com.example.courseWork.repositories.gameSavesRepositories.GameStateParametersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class GameStateParametersService {
    private final GameStateParametersRepository gameStateParametersRepository;

    @Autowired
    public GameStateParametersService(GameStateParametersRepository gameStateParametersRepository) {
        this.gameStateParametersRepository = gameStateParametersRepository;
    }

    public Optional<GameStateParameter> findById(int id){
        return gameStateParametersRepository.findById(id);
    }
}
