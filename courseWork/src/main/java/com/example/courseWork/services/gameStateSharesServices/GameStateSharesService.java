package com.example.courseWork.services.gameStateSharesServices;

import com.example.courseWork.DTO.sharedSaveDTO.GameStateShareResponseDTO;
import com.example.courseWork.DTO.sharedSaveDTO.GameStateSharesResponseDTO;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public GameStateSharesResponseDTO getGameStateShares(int id){
        GameState gameState = gameStatesService.findById(id);
        if(gameState != null){
            GameStateSharesResponseDTO gameStateSharesResponseDTO = new GameStateSharesResponseDTO();
            List<SharedSave> sharedSaves = gameStateSharesRepository.findByGameState(gameState);
            List<GameStateShareResponseDTO> gameStateShares = new LinkedList<>();
            sharedSaves.forEach(
                    sharedSave -> gameStateShares.add(convertToGameStateShareResponseDTO(sharedSave))
            );
            gameStateSharesResponseDTO.setGameStateShares(gameStateShares);
            return  gameStateSharesResponseDTO;
        }
        return null;
    }

    private SharedSave convertToSharedSave(ShareWithDTO shareWithDTO){
        Person person = peopleService.findPersonById(shareWithDTO.getShareWithId());
        GameState gameState = gameStatesService.findById(shareWithDTO.getGameStateId());
        System.out.println(gameState.toString());
        System.out.println(person.toString());
        if(gameState != null && person != null){
            SharedSave sharedSave = new SharedSave(person,gameState);
            person.getSharedSaves().add(sharedSave);
            gameState.getSharedSaves().add(sharedSave);
            /*List<SharedSave> sharedSaves;

            if(person.getSharedSaves()!=null){
                person.getSharedSaves().add(sharedSave);
            }else{
                sharedSaves = new LinkedList<>();
                sharedSaves.add(sharedSave);
                person.setSharedSaves(sharedSaves);
            }

            if(gameState.getSharedSaves()!=null){
                gameState.getSharedSaves().add(sharedSave);
            }else{
                sharedSaves = new LinkedList<>();
                sharedSaves.add(sharedSave);
                gameState.setSharedSaves(sharedSaves);
            }*/
            return sharedSave;
        }
        return null;
    }
    private GameStateShareResponseDTO convertToGameStateShareResponseDTO(SharedSave sharedSave){
        GameStateShareResponseDTO gameStateShareResponseDTO = new GameStateShareResponseDTO(sharedSave.getPerson().getId(),
                sharedSave.getPerson().getUsername());
        return gameStateShareResponseDTO;
    }
    public GameStateShareResponseDTO constructResponseDTO(SharedSave sharedSave){
        return new GameStateShareResponseDTO(
                sharedSave.getPerson().getId(),sharedSave.getPerson().getUsername());
    }
}
