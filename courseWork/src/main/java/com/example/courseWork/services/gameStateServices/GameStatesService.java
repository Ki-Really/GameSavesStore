package com.example.courseWork.services.gameStateServices;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.gameSaveDTO.*;
import com.example.courseWork.converter.GenderConverter;
import com.example.courseWork.converter.TimeConverter;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameModel.GameStateParameter;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.gameSaveModel.GameStateValue;
import com.example.courseWork.models.sharedSave.SharedSave;
import com.example.courseWork.repositories.gameSavesRepositories.GameStatesRepository;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.services.gameServices.GamesService;
import com.example.courseWork.services.gameServices.ImagesService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameStatesService {
    private final GameStatesRepository gameStatesRepository;
    private final GamesService gamesService;
    private final ArchivesService archivesService;
    private final PeopleService peopleService;
    private final ImagesService imagesService;
    private final EntityManager entityManager;
    private final GameStateParametersService gameStateParametersService;
    private final TimeConverter timeConverter;
    private final GenderConverter genderConverter;
    @Autowired
    public GameStatesService(GameStatesRepository gameStatesRepository, GamesService gamesService, ArchivesService archivesService, PeopleService peopleService, ImagesService imagesService, EntityManager entityManager, GameStateParametersService gameStateParametersService, TimeConverter timeConverter, GenderConverter genderConverter) {
        this.gameStatesRepository = gameStatesRepository;
        this.gamesService = gamesService;
        this.archivesService = archivesService;
        this.peopleService = peopleService;
        this.imagesService = imagesService;
        this.entityManager = entityManager;
        this.gameStateParametersService = gameStateParametersService;
        this.timeConverter = timeConverter;
        this.genderConverter = genderConverter;
    }
    public GameState findByName(String name){
        return gameStatesRepository.findByName(name).orElse(null);
    }

    public GameState findById(int id){
        Optional<GameState> gameState = gameStatesRepository.findById(id);
        return gameState.orElse(null);
    }

    public EntitiesResponseDTO<GameStateDTO> findAll(GameStatesRequestDTO gameStatesRequestDTO){
        Page<GameState> page;
        if(gameStatesRequestDTO.getSearchQuery()!=null && !gameStatesRequestDTO.getSearchQuery().isEmpty()){
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

        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = new EntitiesResponseDTO<>();

        gameStatesDTO.setItems(page.getContent().stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }

    public EntitiesResponseDTO<GameStateDTO> findAllByPerson(GameStatesRequestDTO gameStatesRequestDTO, Principal principal){
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

        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = new EntitiesResponseDTO<>();

        gameStatesDTO.setItems(page.getContent().stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }

    public EntitiesResponseDTO<GameStateDTO> findAllPublic(GameStatesRequestDTO gameStatesRequestDTO){
        Page<GameState> page;

        if(gameStatesRequestDTO.getSearchQuery()!=null && !gameStatesRequestDTO.getSearchQuery().isEmpty() && gameStatesRequestDTO.getSearchGameId()>0) {
            page = gameStatesRepository.findByNameContainingAndGameIdAndGameNameContainingAndIsPublicTrue(
                    gameStatesRequestDTO.getSearchQuery(),
                    gameStatesRequestDTO.getSearchGameId(),
                    gameStatesRequestDTO.getSearchQuery(),
                    PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        else if(gameStatesRequestDTO.getSearchQuery()!=null && !gameStatesRequestDTO.getSearchQuery().isEmpty() && gameStatesRequestDTO.getSearchGameId()<=0){
            page = gameStatesRepository.findByNameContainingAndGameNameContainingAndIsPublicTrue(
                    gameStatesRequestDTO.getSearchQuery(),
                    gameStatesRequestDTO.getSearchQuery(),
                    PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        else if((gameStatesRequestDTO.getSearchQuery()==null || gameStatesRequestDTO.getSearchQuery().isEmpty()) && gameStatesRequestDTO.getSearchGameId()>0){
            page = gameStatesRepository.findByNameContainingAndGameIdAndIsPublicTrue(
                    gameStatesRequestDTO.getSearchQuery(),
                    gameStatesRequestDTO.getSearchGameId(),
                    PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        else {
            page = gameStatesRepository.findByIsPublicTrue(PageRequest.of(
                    gameStatesRequestDTO.getPageNumber() - 1,
                    gameStatesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }

        //List<GameState> searched = page.getContent().stream().filter(GameState::getIsPublic).toList();

        /*List<GameState> filtered = filterGameStates(searched,gameStatesRequestDTO);*/

        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = new EntitiesResponseDTO<>();
        gameStatesDTO.setItems(page.stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());



        return gameStatesDTO;
    }

  /*  private List<GameState> filterGameStates(List<GameState> searchedGameStatesDTO,GameStatesRequestDTO gameStatesRequestDTO){
        List<GameState> filteredGameStates;
        if(gameStatesRequestDTO.getSearchGameStateId()>0 && !gameStatesRequestDTO.getSearchQuery().isEmpty()) {
            filteredGameStates = gameStatesRepository.findByIdContainingAndNameContaining(gameStatesRequestDTO.getSearchQuery(), PageRequest.of(
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
        return
    }*/

    /*public EntitiesResponseDTO<GameStateDTO> findAllPublicWithFilters(GameStatesRequestDTO gameStatesRequestDTO){
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
        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = new EntitiesResponseDTO<>();
        List<GameState> filtered = page.getContent().stream().filter(GameState::getIsPublic).toList();

        gameStatesDTO.setItems(filtered.stream().map(
                this::constructGameStateDTO
        ).toList());
        gameStatesDTO.setTotalCount(page.getTotalElements());

        return gameStatesDTO;
    }*/

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
    public EntitiesResponseDTO<GameStateDTO> findReceivedGameStates(GameStatesRequestDTO gameStatesRequestDTO, Principal principal) {
        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = new EntitiesResponseDTO<>();
        Person person = peopleService.findOne(principal.getName());
        List<SharedSave> sharedSaves = person.getSharedSaves();

        if (!sharedSaves.isEmpty()) {
            List<GameState> receivedGameStates = sharedSaves.stream()
                    .map(SharedSave::getGameState)
                    .collect(Collectors.toList());

            String searchName = gameStatesRequestDTO.getSearchQuery();
            if (searchName != null && !searchName.isEmpty()) {
                receivedGameStates = receivedGameStates.stream()
                        .filter(state -> state.getName().toLowerCase().contains(searchName.toLowerCase()))
                        .collect(Collectors.toList());
            }

            Pageable pageableAll = PageRequest.of(gameStatesRequestDTO.getPageNumber() - 1, gameStatesRequestDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
            int startAll = (int) pageableAll.getOffset();
            int endAll = Math.min((startAll + pageableAll.getPageSize()), receivedGameStates.size());

            Page<GameState> gameStatePageAll = new PageImpl<>(receivedGameStates.subList(startAll, endAll), pageableAll, receivedGameStates.size());

            gameStatesDTO.setTotalCount(gameStatePageAll.getTotalElements());
            gameStatesDTO.setItems(gameStatePageAll.getContent().stream()
                    .map(this::constructGameStateDTO)
                    .toList());

            if (!receivedGameStates.isEmpty()) {
                Pageable pageable = PageRequest.of(gameStatesRequestDTO.getPageNumber() - 1, gameStatesRequestDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), receivedGameStates.size());

                Page<GameState> gameStatePage = new PageImpl<>(receivedGameStates.subList(start, end), pageable, receivedGameStates.size());

                gameStatesDTO.setTotalCount(gameStatePage.getTotalElements());
                gameStatesDTO.setItems(gameStatePage.getContent().stream()
                        .map(this::constructGameStateDTO)
                        .toList());
            }
        }else{
            gameStatesDTO.setItems(new ArrayList<>());
            gameStatesDTO.setTotalCount(0);
        }
        return gameStatesDTO;
    }
    @Transactional
    public int save(GameStateRequestDTO gameStateRequestDTO, MultipartFile file, Principal principal) {
        String fileName = generateFilename(file);
        archivesService.upload(file,fileName);
        GameState gameState = convertGameState(gameStateRequestDTO,file,fileName,principal);
        gameStatesRepository.save(gameState);
        return gameState.getId();
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
        gameStateDTO.setGameName(gameState.getGame().getName());
        gameStateDTO.setCreatedAt(gameState.getCreatedAt());
        gameStateDTO.setUpdatedAt(gameState.getUpdatedAt());
        gameStateDTO.setUploadedAt(gameState.getUploadedAt());
        return gameStateDTO;
    }

////??????????????????????????????????????????????????????????????????\/\/\/
    private List<GameStateValue> convertToGameStateValues(List<GameStateValueDTO> gameStateValueDTOS,int gameId,GameState gameState){
        List<GameStateValue> listToReturn = new LinkedList<>();
        String convertedValueInSeconds;
        String convertedValueGender;
        for(int i = 0; i<gameStateValueDTOS.size();i++){
            GameStateValue gameStateValue = new GameStateValue();
            GameStateValueDTO gameStateValueDTO = gameStateValueDTOS.get(i);
            Optional<GameStateParameter> optionalGameStateParameter = gameStateParametersService.findById(gameStateValueDTO.getGameStateParameterId());

            if(optionalGameStateParameter.isPresent()){
                GameStateParameter gameStateParameter = optionalGameStateParameter.get();
                if(gameStateParameter.getCommonParameter() != null && gameStateParameter.getCommonParameter().getGameStateParameterType().getType().equals("time_seconds")){
                    convertedValueInSeconds = timeConverter.convert(gameStateParameter.getGameStateParameterType().getType(),gameStateValueDTO.getValue());
                    gameStateValue.setValue(convertedValueInSeconds);
                }else if(gameStateParameter.getCommonParameter() != null && gameStateParameter.getCommonParameter().getGameStateParameterType().getType().equals("gender")){
                    convertedValueGender = genderConverter.convert(gameStateValueDTO.getValue());
                    gameStateValue.setValue(convertedValueGender);
                }
                    else{
                    gameStateValue.setValue(gameStateValueDTO.getValue());
                }
            }
            //найти по id gameStateParameter
            //проверить есть ли commonParameter с id у найденного gameStateParameter.
            //сконвертировать все данные с gameStateParameter в тип commonParameter и создать gameStateValue\/\/\/

            gameStateValue.setGameStateParameter(gamesService.findOne(gameId).getScheme().getGameStateParameters().get(i));
            gameStateValue.setGameState(gameState);
            if(gameStateValueDTO.getId() != 0){
                gameStateValue.setId(gameStateValueDTO.getId());
            }
            listToReturn.add(gameStateValue);
        }
        return listToReturn;
    }

    private List<GameStateValueDTO> convertToGameStateValuesDTO(List<GameStateValue> gameStateValues){
        List<GameStateValueDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateValues.size();i++){

            GameStateValue gameStateValue = gameStateValues.get(i);

            GameStateValueDTO gameStateValueDTO = new GameStateValueDTO(
                    gameStateValue.getGameStateParameter().getId(), gameStateValue.getValue());
                    gameStateValueDTO.setLabel(gameStateValue.getGameStateParameter().getLabel()
            );
            gameStateValueDTO.setDescription(
                    gameStateValue.getGameStateParameter().getDescription()
            );
            gameStateValueDTO.setGameStateParameterType(convertToGameStateParameterTypeDTO(
                    gameStateValue.getGameStateParameter().getGameStateParameterType())
            );

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
