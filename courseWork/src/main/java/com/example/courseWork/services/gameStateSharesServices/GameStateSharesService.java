package com.example.courseWork.services.gameStateSharesServices;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.sharedSaveDTO.GameStateShareResponseDTO;
import com.example.courseWork.DTO.sharedSaveDTO.ShareWithDTO;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.sharedSave.SharedSave;
import com.example.courseWork.repositories.gameStateSharesRepositories.GameStateSharesRepository;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.services.gameStateServices.GameStatesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class GameStateSharesService {
    private final GameStateSharesRepository gameStateSharesRepository;
    private final GameStatesService gameStatesService;
    private final PeopleService peopleService;

    @Autowired
    public GameStateSharesService(GameStateSharesRepository gameStateSharesRepository, GameStatesService gameStatesService, PeopleService peopleService) {
        this.gameStateSharesRepository = gameStateSharesRepository;
        this.gameStatesService = gameStatesService;
        this.peopleService = peopleService;
    }
    @Transactional
    public SharedSave save(ShareWithDTO shareWithDTO){
        SharedSave sharedSave = convertToSharedSave(shareWithDTO);
        if(sharedSave != null){
            gameStateSharesRepository.save(sharedSave);
            return sharedSave;
        }
        return null;
    }
    @Transactional
    public void deleteById(int id){
        gameStateSharesRepository.deleteById(id);
    }

    public EntitiesResponseDTO<GameStateShareResponseDTO> getGameStateShares(int id){
        GameState gameState = gameStatesService.findById(id);
        if(gameState != null){
            EntitiesResponseDTO<GameStateShareResponseDTO> gameStateSharesResponseDTO = new EntitiesResponseDTO<>();
            List<SharedSave> sharedSaves = gameStateSharesRepository.findByGameState(gameState);
            List<GameStateShareResponseDTO> gameStateShares = new LinkedList<>();
            sharedSaves.forEach(
                    sharedSave -> gameStateShares.add(convertToGameStateShareResponseDTO(sharedSave))
            );
            gameStateSharesResponseDTO.setItems(gameStateShares);
            gameStateSharesResponseDTO.setTotalCount(gameStateShares.size());
            return  gameStateSharesResponseDTO;
        }
        return null;
    }

    private SharedSave convertToSharedSave(ShareWithDTO shareWithDTO){
        Person person = peopleService.findPersonById(shareWithDTO.getShareWithId());
        GameState gameState = gameStatesService.findById(shareWithDTO.getGameStateId());

        if(gameState != null && person != null){
            SharedSave sharedSave = new SharedSave(person,gameState);
            person.getSharedSaves().add(sharedSave);
            gameState.getSharedSaves().add(sharedSave);
            return sharedSave;
        }
        return null;
    }
    private GameStateShareResponseDTO convertToGameStateShareResponseDTO(SharedSave sharedSave){
        GameStateShareResponseDTO gameStateShareResponseDTO = new GameStateShareResponseDTO(sharedSave.getId(),
                sharedSave.getPerson().getUsername());
        return gameStateShareResponseDTO;
    }
    public GameStateShareResponseDTO constructResponseDTO(SharedSave sharedSave){
        return new GameStateShareResponseDTO(
                sharedSave.getPerson().getId(),sharedSave.getPerson().getUsername());
    }


}
