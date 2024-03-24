package com.example.courseWork.services.gameStateServices;

import com.example.courseWork.DTO.gameDTO.GamesRequestDTO;
import com.example.courseWork.DTO.gameDTO.GamesResponseDTO;
import com.example.courseWork.DTO.gameSaveDTO.*;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.gameSaveModel.GameStateValue;
import com.example.courseWork.repositories.gameSavesRepositories.GameStatesRepository;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.services.gameServices.GamesService;
import com.example.courseWork.services.gameServices.ImagesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class GameStatesService {
    private final GameStatesRepository gameStatesRepository;
    private final GamesService gamesService;
    private final ArchivesService archivesService;
    private final PeopleService peopleService;
    private final ImagesService imagesService;
    @Autowired
    public GameStatesService(GameStatesRepository gameStatesRepository, GamesService gamesService, ArchivesService archivesService, PeopleService peopleService, ImagesService imagesService) {
        this.gameStatesRepository = gameStatesRepository;
        this.gamesService = gamesService;
        this.archivesService = archivesService;
        this.peopleService = peopleService;
        this.imagesService = imagesService;
    }
    public GameState findByName(String name){
        return gameStatesRepository.findByName(name).orElse(null);
    }

    public GameState findById(int id){
        Optional<GameState> gameState = gameStatesRepository.findById(id);
        return gameState.orElse(null);
    }

    public GameStatesDTO findAll(GameStatesRequestDTO gameStatesRequestDTO){
        Page<GameState> page = gameStatesRepository.findAll(PageRequest.of(
                gameStatesRequestDTO.getPageNumber() - 1,
                gameStatesRequestDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        ));
        GameStatesDTO gameStatesDTO = new GameStatesDTO();

        gameStatesDTO.setItems(page.getContent().stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }
    @Transactional
    public void save(GameStateRequestDTO gameStateRequestDTO, MultipartFile file, Principal principal) {
        String fileName = generateFilename(file);
        archivesService.upload(file,fileName);
        GameState gameState = convertGameState(gameStateRequestDTO,file,fileName,principal);
        gameStatesRepository.save(gameState);
    }
    @Transactional
    public void deleteById(int id){
        Optional<GameState> optionalGameState = gameStatesRepository.findById(id);

        if(optionalGameState.isPresent()){
            GameState gameState = optionalGameState.get();
            if(gameState.getArchiveName() != null){
                archivesService.removeFile(gameState.getArchiveName());
            }
            gameStatesRepository.delete(gameState);
        }
    }
    @Transactional
    public void update(GameStateRequestDTO gameStateRequestDTO,MultipartFile file, int id,Principal principal){
        Optional<GameState> optionalGameState = gameStatesRepository.findById(id);
        if(optionalGameState.isPresent()){
            GameState gameState = optionalGameState.get();
            String archiveName = gameState.getArchiveName();
            GameState updatedGameState = convertGameState(gameStateRequestDTO,file,archiveName,principal);

            updatedGameState.setId(gameState.getId());
            archivesService.update(file,updatedGameState);
            updatedGameState.setUpdatedAt(LocalDateTime.now());
            gameStatesRepository.save(updatedGameState);

        }
    }

    private String generateFilename(MultipartFile file){
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }
    private String getExtension(MultipartFile file){
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
    }

    private GameState convertGameState(GameStateRequestDTO gameStateRequestDTO, MultipartFile file, String archiveName, Principal principal){
        GameState gameState = new GameState();
        gameState.setName(gameStateRequestDTO.getName());
        gameState.setPublic(false);
        gameState.setLocalPath(gameStateRequestDTO.getLocalPath());
        gameState.setGameStateValues(convertToGameStateValues(gameStateRequestDTO.getGameStateValues(),gameStateRequestDTO.getGameId(),gameState));
        gameState.setArchiveName(archiveName);
        gameState.setGame(gamesService.findOne(gameStateRequestDTO.getGameId()));

        try {
            gameState.setSizeInBytes(file.getBytes().length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameState.setPerson(peopleService.findOne(principal.getName()));
        gameState.setPublic(false);
        gameState.setCreatedAt(LocalDateTime.now());
        gameState.setUpdatedAt(LocalDateTime.now());
        gameState.setUploadedAt(LocalDateTime.now());
        return gameState;
    }
    public GameStateDTO constructGameStateDTO(GameState gameState){
        GameStateDTO gameStateDTO = new GameStateDTO();
        gameStateDTO.setId(gameState.getId());
        gameStateDTO.setName(gameState.getName());
        gameStateDTO.setGameId(gameState.getGame().getId());
        gameStateDTO.setArchiveUrl(archivesService.getFileUrl(gameState.getArchiveName()));
        gameStateDTO.setLocalPath(gameState.getLocalPath());
        gameStateDTO.setGameIconUrl(imagesService.getFileUrl(gameState.getGame().getImage().getId()));
        gameStateDTO.setSizeInBytes(gameState.getSizeInBytes());
        gameStateDTO.setGameStateValues(convertToGameStateValuesDTO(gameState.getGameStateValues()));
        return gameStateDTO;
    }

    private List<GameStateValue> convertToGameStateValues(List<GameStateValueDTO> gameStateValueDTO,int gameId,GameState gameState){
        List<GameStateValue> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateValueDTO.size();i++){
            GameStateValue gameStateValue = new GameStateValue(gameStateValueDTO.get(i).getValue(),
                    gamesService.findOne(gameId).getScheme().getGameStateParameters().get(i));
            gameStateValue.setGameState(gameState);
            if(gameStateValueDTO.get(i).getId() != 0){
                gameStateValue.setId(gameStateValueDTO.get(i).getId());
            }
            listToReturn.add(gameStateValue);
        }
        return listToReturn;
    }

    private List<GameStateValueDTO> convertToGameStateValuesDTO(List<GameStateValue> gameStateValues){
        List<GameStateValueDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateValues.size();i++){
            GameStateValueDTO gameStateValueDTO = new GameStateValueDTO(gameStateValues.get(i).getGameStateParameter().getId(), gameStateValues.get(i).getValue());
            listToReturn.add(gameStateValueDTO);
        }
        return listToReturn;
    }

}
