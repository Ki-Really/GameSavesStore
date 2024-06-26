package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.gameStateParameterTypeDTO.GameStateParameterTypesRequestDTO;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.repositories.gameRepositories.GameStateParameterTypesRepository;
import com.example.courseWork.util.exceptions.gameStateParameterTypeException.GameStateParameterTypeNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return optionalGameStateParameterType.orElseThrow(() ->
                new GameStateParameterTypeNotFoundException("Game state parameter type not found with type " + type));
    }
    public GameStateParameterType findById(int id){
        Optional<GameStateParameterType> optionalGameStateParameterType = gameStateParameterTypesRepository.findById(id);
        return optionalGameStateParameterType.orElseThrow(() ->
                new GameStateParameterTypeNotFoundException("Game state parameter type not found with this id!"));
    }
    public EntitiesResponseDTO<GameStateParameterTypeDTO> findAll(GameStateParameterTypesRequestDTO gameStateParameterTypesRequestDTO){
        Page<GameStateParameterType> page;
        if(gameStateParameterTypesRequestDTO.getSearchQuery()!= null &&
                !gameStateParameterTypesRequestDTO.getSearchQuery().isEmpty()){
            page = gameStateParameterTypesRepository.findByTypeContaining(
                    gameStateParameterTypesRequestDTO.getSearchQuery(), PageRequest.of(
                    gameStateParameterTypesRequestDTO.getPageNumber() - 1,
                    gameStateParameterTypesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        } else{
            page = gameStateParameterTypesRepository.findAll(PageRequest.of(
                    gameStateParameterTypesRequestDTO.getPageNumber() - 1,
                    gameStateParameterTypesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        EntitiesResponseDTO<GameStateParameterTypeDTO> gameStateParameterTypesResponseDTO = new EntitiesResponseDTO<>();

        gameStateParameterTypesResponseDTO.setItems(page.getContent().stream().map(
                this::constructGameStateParameterType
        ).toList());
        gameStateParameterTypesResponseDTO.setTotalCount(page.getTotalElements());

        return gameStateParameterTypesResponseDTO;
    }

    private GameStateParameterTypeDTO constructGameStateParameterType(GameStateParameterType gameStateParameterType) {
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }
}
