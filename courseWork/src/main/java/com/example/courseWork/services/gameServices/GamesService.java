package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.gameModel.*;
import com.example.courseWork.repositories.gameRepositories.GamesRepository;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
/*
delete from path;
        delete from extractionpipeline;
        delete from image;
        delete from game_state_parameter;
        delete from scheme;
        delete from game;
        */

@Service
@Transactional
public class GamesService {
    private final GamesRepository gamesRepository;
    private final ImagesService imagesService;
    private final GameStateParameterTypesService gameStateParameterTypesService;

    private final CommonParametersService commonParametersService;
    private final PathsService pathsService;
    @Autowired
    public GamesService(GamesRepository gamesRepository, ImagesService imagesService, GameStateParameterTypesService gameStateParameterTypesService, PathsService pathsService, CommonParametersService commonParametersService) {
        this.gamesRepository = gamesRepository;
        this.imagesService = imagesService;
        this.gameStateParameterTypesService = gameStateParameterTypesService;
        this.pathsService = pathsService;
        this.commonParametersService = commonParametersService;
    }

    public Game findByName(String name){
        Optional<Game> game = gamesRepository.findByName(name);
        return game.orElse(null);
    }
    public Game findOne(int id){
        Optional<Game> game = gamesRepository.findById(id);
        return game.orElse(null);
    }

    public EntitiesResponseDTO<GameResponseDTO> findAll(GamesRequestDTO gamesRequestDTO){

        Page<Game> page;
        if (gamesRequestDTO.getSearchQuery() != null && !gamesRequestDTO.getSearchQuery().isEmpty()) {
            page = gamesRepository.findByNameContainingOrDescriptionContaining(
                    gamesRequestDTO.getSearchQuery(), gamesRequestDTO.getSearchQuery(),
                    PageRequest.of(gamesRequestDTO.getPageNumber() - 1, gamesRequestDTO.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")));
        } else {
            page = gamesRepository.findAll(PageRequest.of(
                    gamesRequestDTO.getPageNumber() - 1,
                    gamesRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        EntitiesResponseDTO<GameResponseDTO> gamesResponseDTO = new EntitiesResponseDTO<>();

        gamesResponseDTO.setItems(page.getContent().stream().map(
            this::constructGame
        ).toList());
        gamesResponseDTO.setTotalCount(page.getTotalElements());

        return gamesResponseDTO;
    }



    @Transactional
    public void deleteById(int id) {
        Optional<Game> optionalGame = gamesRepository.findById(id);

        if(optionalGame.isPresent()){
            Game game = optionalGame.get();
            if(game.getImage() != null){
                imagesService.removeFile(game.getImage());
            }
            gamesRepository.delete(game);
        }
    }

    public GameResponseDTO constructGame(Game game){
        GameResponseDTO gameResponseDTO = new GameResponseDTO();
        gameResponseDTO.setId(game.getId());
        gameResponseDTO.setName(game.getName());
        gameResponseDTO.setDescription(game.getDescription());
        gameResponseDTO.setPaths(convertToPathDTO(game.getPaths()));
        gameResponseDTO.setExtractionPipeline(convertToExtractionPipelineDTO(game.getExtractionPipelines()));

        Scheme scheme = game.getScheme();

        List<GameStateParameterResponseDTO> gameStateParameterResponseDTOS = convertToGameStateParameterResponseDTO(scheme.getGameStateParameters());

        SchemeResponseDTO schemeResponseDTO = convertToSchemeResponseDTO(scheme);
        schemeResponseDTO.setGameStateParameters(gameStateParameterResponseDTOS);

        gameResponseDTO.setSchema(schemeResponseDTO);
        String url = imagesService.getFileUrl(game.getImage().getId());
        gameResponseDTO.setImageUrl(url);
        return gameResponseDTO;
    }

    @Transactional
    public void save(GameRequestDTO gameRequestDTO, MultipartFile file) {
        Game game = convertGame(gameRequestDTO,file);
        imagesService.upload(file, game.getImage().getName());
        gamesRepository.save(game);
    }

    @Transactional
    public void update(GameRequestDTO gameRequestDTO, MultipartFile file, int id){
        Optional<Game> optionalGame = gamesRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            Game updatedGame = convertGame(gameRequestDTO,file);

            updatedGame.getScheme().setId(game.getScheme().getId());
            updatedGame.getImage().setId(game.getImage().getId());
            updatedGame.getImage().setName(game.getImage().getName());

            updatedGame.setId(game.getId());
            gamesRepository.save(updatedGame);

            if (file != null) {
                imagesService.update(file,updatedGame);
            }
        } else {
            throw new EntityNotFoundException("Game with id " + id + " not found");
        }
    }

    private Game convertGame(GameRequestDTO gameRequestDTO,MultipartFile file){
        Game game = new Game(gameRequestDTO.getName(),gameRequestDTO.getDescription());

        List<Path> paths = convertToPath(gameRequestDTO.getPaths(),game);

        game.setPaths(paths);

        List<ExtractionPipeline> extractionPipelines= convertToExtractionPipeline(gameRequestDTO.getExtractionPipeline(),game);
        game.setExtractionPipelines(extractionPipelines);

        Scheme scheme = convertToScheme(gameRequestDTO.getSchema());
        scheme.setGame(game);

        List<GameStateParameter> gameStateParameters = convertToGameStateParameter(gameRequestDTO.getSchema().getGameStateParameters(),scheme);
        scheme.setGameStateParameters(gameStateParameters);
        game.setScheme(scheme);

        String filename = generateFilename(file);
        Image image = new Image(filename);

        game.setImage(image);
        image.setGame(game);
        return game;
    }
    private String generateFilename(MultipartFile file){
        if (file == null) {
            return  "";
        }

        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }
    private String getExtension(MultipartFile file){
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
    }

    private List<Path> convertToPath(List<PathDTO> pathDTO,Game game){
        List<Path> listToReturn = new LinkedList<>();
        for(int i = 0; i<pathDTO.size(); i++){
            Path path = new Path(pathDTO.get(i).getPath());
            if(pathDTO.get(i).getId() != 0){
                path.setId(pathDTO.get(i).getId());
            }
            path.setGame(game);
            listToReturn.add(path);
        }
        return listToReturn;
    }

    private List<ExtractionPipeline> convertToExtractionPipeline(List<ExtractionPipelineDTO> extractionPipelineDTOS,Game game){
        List<ExtractionPipeline> listToReturn = new LinkedList<>();
        for(int i = 0; i<extractionPipelineDTOS.size(); i++){
            ExtractionPipelineDTO pipelineDTO = extractionPipelineDTOS.get(i);
            ExtractionPipeline pipeline = new ExtractionPipeline(pipelineDTO.getType(),
                    pipelineDTO.getInputFilename(),
                    pipelineDTO.getOutputFilename());
            if(pipelineDTO.getId() != 0){
                pipeline.setId(pipelineDTO.getId());
            }
            pipeline.setGame(game);
            listToReturn.add(pipeline);
        }
        return listToReturn;
    }

    private List<GameStateParameter> convertToGameStateParameter(List<GameStateParameterDTO> gameStateParameterDTOS, Scheme scheme){
        List<GameStateParameter> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateParameterDTOS.size(); i++){
            GameStateParameterDTO gameStateParameterDTO = gameStateParameterDTOS.get(i);
            GameStateParameter gameStateParameter = new GameStateParameter(
                    gameStateParameterDTO.getKey(),
                    gameStateParameterDTO.getLabel(),
                    gameStateParameterDTO.getDescription()
            );

            if(gameStateParameterDTO.getId() != 0){
                gameStateParameter.setId(gameStateParameterDTO.getId());
            }

            gameStateParameter.setScheme(scheme);

            GameStateParameterType gameStateParameterType = gameStateParameterTypesService.findByType(gameStateParameterDTO.getType());
            gameStateParameter.setGameStateParameterType(gameStateParameterType);

            if(gameStateParameterDTO.getCommonParameterId()>0){
                CommonParameter commonParameter = commonParametersService.findById(
                        gameStateParameterDTO.getCommonParameterId()
                );
                gameStateParameter.setCommonParameter(commonParameter);
            }

            List<GameStateParameter> gameStateParameters;
            if(gameStateParameterType.getGameStateParameters() != null){
                gameStateParameters = gameStateParameterType.getGameStateParameters();
            }else{
                gameStateParameters = new LinkedList<>();
            }
            gameStateParameters.add(gameStateParameter);
            gameStateParameterType.setGameStateParameters(gameStateParameters);

            listToReturn.add(gameStateParameter);
        }
        return listToReturn;
    }

    private List<GameStateParameterResponseDTO> convertToGameStateParameterResponseDTO(List<GameStateParameter> gameStateParameters){
        List<GameStateParameterResponseDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateParameters.size(); i++){
            GameStateParameter gameStateParameter = gameStateParameters.get(i);
            GameStateParameterResponseDTO gameStateParameterResponseDTO = new GameStateParameterResponseDTO(
                    gameStateParameter.getKey(),
                    gameStateParameter.getGameStateParameterType().getType(),
                    gameStateParameter.getLabel(),gameStateParameter.getDescription()
            );
            gameStateParameterResponseDTO.setId(gameStateParameter.getId());

            CommonParameter commonParameter = gameStateParameter.getCommonParameter();
            if (commonParameter != null) {
                gameStateParameterResponseDTO.setCommonParameterDTO(constructCommonParameterDTO(commonParameter));
            }
            listToReturn.add(gameStateParameterResponseDTO);
        }
        return listToReturn;
    }
/*    private CommonParameterDTO convertToCommonParameterDTO(CommonParameter commonParameter){
        CommonParameterDTO commonParameterDTO = new CommonParameterDTO();
        commonParameterDTO.setId(commonParameter.getId());
        commonParameterDTO.setLabel(commonParameter.getLabel());
        commonParameterDTO.setDescription(commonParameter.getDescription());
        commonParameterDTO.setGameStateParameterTypeDTO(convertToGameStateParameterTypeDTO(commonParameter.getGameStateParameterType()));
        return commonParameterDTO;
    }*/

/*    private GameStateParameterTypeDTO convertToGameStateParameterTypeDTO(GameStateParameterType gameStateParameterType){
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }*/
    private Scheme convertToScheme(SchemeDTO schemeDTO){
        Scheme scheme = new Scheme(schemeDTO.getFilename());
        return scheme;
    }

    private SchemeResponseDTO convertToSchemeResponseDTO(Scheme scheme){
        SchemeResponseDTO schemeResponseDTO = new SchemeResponseDTO(
                scheme.getFilename(),
                convertToGameStateParameterResponseDTO(scheme.getGameStateParameters())
        );
        return schemeResponseDTO;
    }

    private List<PathDTO> convertToPathDTO(List<Path> paths){
        List<PathDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<paths.size(); i++){
            Path path = paths.get(i);
            PathDTO pathDTO = new PathDTO(path.getPath());
            pathDTO.setId(path.getId());
            listToReturn.add(pathDTO);
        }
        return listToReturn;
    }
    private List<ExtractionPipelineDTO> convertToExtractionPipelineDTO(List<ExtractionPipeline> extractionPipelines){
        List<ExtractionPipelineDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<extractionPipelines.size(); i++){
            ExtractionPipeline extractionPipeline = extractionPipelines.get(i);
            ExtractionPipelineDTO extractionPipelineDTO = new ExtractionPipelineDTO(
                    extractionPipeline.getType(),
                    extractionPipeline.getInputFilename(),
                    extractionPipeline.getOutputFilename());
            extractionPipelineDTO.setId(extractionPipeline.getId());
            listToReturn.add(extractionPipelineDTO);
        }
        return listToReturn;
    }
    private CommonParameterDTO constructCommonParameterDTO(CommonParameter commonParameter){
        CommonParameterDTO commonParameterDTO = new CommonParameterDTO();
        commonParameterDTO.setLabel(commonParameter.getLabel());
        commonParameterDTO.setDescription(commonParameter.getDescription());
        commonParameterDTO.setId(commonParameter.getId());

        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(commonParameter.getGameStateParameterType().getId());
        gameStateParameterTypeDTO.setType(commonParameter.getGameStateParameterType().getType());
        commonParameterDTO.setGameStateParameterTypeDTO(gameStateParameterTypeDTO);
        return commonParameterDTO;
    }
}
