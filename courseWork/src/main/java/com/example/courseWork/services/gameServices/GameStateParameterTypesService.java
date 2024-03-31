package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.gameStateParameterTypeDTO.GameStateParameterTypesResponseDTO;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.repositories.gameRepositories.GameStateParameterTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class GameStateParameterTypesService {
    private final GameStateParameterTypesRepository gameStateParameterTypesRepository;

    @Autowired
    public GameStateParameterTypesService(GameStateParameterTypesRepository gameStateParameterTypesRepository) {
        this.gameStateParameterTypesRepository = gameStateParameterTypesRepository;
    }

    public GameStateParameterType findByType(String type){
        Optional<GameStateParameterType> optionalGameStateParameterType = gameStateParameterTypesRepository.findByType(type);
        return optionalGameStateParameterType.orElse(null);
    }
    public GameStateParameterType findById(int id){
        Optional<GameStateParameterType> optionalGameStateParameterType = gameStateParameterTypesRepository.findById(id);
        return optionalGameStateParameterType.orElse(null);
    }
    public GameStateParameterTypesResponseDTO findAll(){

    }
}
