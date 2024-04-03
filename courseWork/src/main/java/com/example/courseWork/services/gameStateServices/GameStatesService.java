package com.example.courseWork.services.gameStateServices;

import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.gameSaveDTO.*;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.gameSaveModel.GameStateValue;
import com.example.courseWork.models.sharedSave.SharedSave;
import com.example.courseWork.repositories.gameSavesRepositories.GameStatesRepository;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.services.gameServices.GamesService;
import com.example.courseWork.services.gameServices.ImagesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public GameStatesDTO findAll(GameStatesRequestDTO gameStatesRequestDTO,Principal principal){
        Person person = peopleService.findOne(principal.getName());
        Page<GameState> page;
        if(gameStatesRequestDTO.getSearchQuery()!=null && !gameStatesRequestDTO.getSearchQuery().isEmpty()){
            page = gameStatesRepository.findByPersonIdAndNameContaining(person.getId(),gameStatesRequestDTO.getSearchQuery(), PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }else{
            page = gameStatesRepository.findByPersonId(person.getId(),PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }

        GameStatesDTO gameStatesDTO = new GameStatesDTO();

        gameStatesDTO.setItems(page.getContent().stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }
    public GameStatesDTO findAllPublic(GameStatesRequestDTO gameStatesRequestDTO){
        Page<GameState> page;
        if(gameStatesRequestDTO.getSearchQuery()!=null && !gameStatesRequestDTO.getSearchQuery().isEmpty()) {
            page = gameStatesRepository.findByNameContaining(gameStatesRequestDTO.getSearchQuery(), PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }else{
            page = gameStatesRepository.findAll(PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        GameStatesDTO gameStatesDTO = new GameStatesDTO();
        List<GameState> filtered = page.getContent().stream().filter(GameState::getIsPublic).toList();

        gameStatesDTO.setItems(filtered.stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }

    /*public GameStatesDTO findReceivedGameStates(GameStatesRequestDTO gameStatesRequestDTO,Principal principal){
        GameStatesDTO gameStatesDTO = new GameStatesDTO();
        Person person = peopleService.findOne(principal.getName());
        List<SharedSave> sharedSaves = person.getSharedSaves();

        if (!sharedSaves.isEmpty()) {
            Pageable pageable = PageRequest.of(gameStatesRequestDTO.getPageNumber()-1, gameStatesRequestDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

            List<GameState> receivedGameStates = sharedSaves.stream()
                    .map(SharedSave::getGameState)
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), receivedGameStates.size());

            Page<GameState> gameStatePage = new PageImpl<>(receivedGameStates.subList(start, end), pageable, receivedGameStates.size());

            gameStatesDTO.setTotalCount(gameStatePage.getTotalElements());

            gameStatesDTO.setItems(gameStatePage.getContent().stream()
                    .map(this::constructGameStateDTO)
                    .toList());

            return gameStatesDTO;
        }
        return new GameStatesDTO();
    }*/
    public GameStatesDTO findReceivedGameStates(GameStatesRequestDTO gameStatesRequestDTO, Principal principal) {
        GameStatesDTO gameStatesDTO = new GameStatesDTO();
        Person person = peopleService.findOne(principal.getName());
        List<SharedSave> sharedSaves = person.getSharedSaves();

        if (!sharedSaves.isEmpty()) {
            List<GameState> receivedGameStates = sharedSaves.stream()
                    .map(SharedSave::getGameState)
                    .collect(Collectors.toList());

            // Фильтруем список по имени игрового сохранения
            String searchName = gameStatesRequestDTO.getSearchQuery();
            if (searchName != null && !searchName.isEmpty()) {
                receivedGameStates = receivedGameStates.stream()
                        .filter(state -> state.getName().toLowerCase().contains(searchName.toLowerCase()))
                        .collect(Collectors.toList());
            }

            // Применяем пагинацию для всех игровых сохранений, если нет результатов поиска
            Pageable pageableAll = PageRequest.of(gameStatesRequestDTO.getPageNumber() - 1, gameStatesRequestDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
            int startAll = (int) pageableAll.getOffset();
            int endAll = Math.min((startAll + pageableAll.getPageSize()), receivedGameStates.size());

            Page<GameState> gameStatePageAll = new PageImpl<>(receivedGameStates.subList(startAll, endAll), pageableAll, receivedGameStates.size());

            gameStatesDTO.setTotalCount(gameStatePageAll.getTotalElements());
            gameStatesDTO.setItems(gameStatePageAll.getContent().stream()
                    .map(this::constructGameStateDTO)
                    .toList());

            // Проверяем, есть ли результаты поиска
            if (!receivedGameStates.isEmpty()) {
                // Применяем пагинацию для найденных результатов
                Pageable pageable = PageRequest.of(gameStatesRequestDTO.getPageNumber() - 1, gameStatesRequestDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), receivedGameStates.size());

                Page<GameState> gameStatePage = new PageImpl<>(receivedGameStates.subList(start, end), pageable, receivedGameStates.size());

                gameStatesDTO.setTotalCount(gameStatePage.getTotalElements());
                gameStatesDTO.setItems(gameStatePage.getContent().stream()
                        .map(this::constructGameStateDTO)
                        .toList());
            }
        }
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
    public void deleteById(int id,Principal principal){
        Optional<GameState> optionalGameState = gameStatesRepository.findById(id);
        if(optionalGameState.isPresent()){
            GameState gameState = optionalGameState.get();
            if(gameState.getPerson().getUsername().equals(principal.getName())){
                if(gameState.getArchiveName() != null){
                    archivesService.removeFile(gameState.getArchiveName());
                }
                gameStatesRepository.delete(gameState);
            }
        }
    }
    @Transactional
    public void update(GameStateRequestDTO gameStateRequestDTO,MultipartFile file, int id,Principal principal){
        Optional<GameState> optionalGameState = gameStatesRepository.findById(id);
        if(optionalGameState.isPresent()){
            GameState gameState = optionalGameState.get();
            if(gameState.getPerson().getUsername().equals(principal.getName())){
                String archiveName = gameState.getArchiveName();
                GameState updatedGameState = convertGameState(gameStateRequestDTO,file,archiveName,principal);

                updatedGameState.setId(gameState.getId());

                archivesService.update(file,updatedGameState);
                updatedGameState.setUpdatedAt(LocalDateTime.now());
                gameStatesRepository.save(updatedGameState);
            }
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
        gameState.setIsPublic(gameStateRequestDTO.getIsPublic());

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
        gameStateDTO.setIsPublic(gameState.getIsPublic());
        gameStateDTO.setCreatedAt(gameState.getCreatedAt());
        gameStateDTO.setUpdatedAt(gameState.getUpdatedAt());
        gameStateDTO.setUploadedAt(gameState.getUploadedAt());
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
            gameStateValueDTO.setLabel(gameStateValues.get(i).getGameStateParameter().getLabel());
            gameStateValueDTO.setDescription(gameStateValues.get(i).getGameStateParameter().getDescription());
            gameStateValueDTO.setGameStateParameterType(convertToGameStateParameterTypeDTO(gameStateValues.get(i).getGameStateParameter().getGameStateParameterType()));

            listToReturn.add(gameStateValueDTO);
        }
        return listToReturn;
    }
    private GameStateParameterTypeDTO convertToGameStateParameterTypeDTO(GameStateParameterType gameStateParameterType){
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }

}
