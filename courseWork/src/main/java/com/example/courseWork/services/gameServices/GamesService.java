package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.gameModel.*;
import com.example.courseWork.repositories.gameRepositories.GamesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    @Autowired
    public GamesService(GamesRepository gamesRepository, ImagesService imagesService) {
        this.gamesRepository = gamesRepository;
        this.imagesService = imagesService;
    }

    public Game findByName(String name){
        Optional<Game> game = gamesRepository.findByName(name);
        return game.orElse(null);
    }
    public Game findOne(int id){
        Optional<Game> game = gamesRepository.findById(id);
        return game.orElse(null);
    }

    public GamesResponseDTO findAll(GamesRequestDTO gamesRequestDTO){
        Page<Game> page = gamesRepository.findAll(PageRequest.of(
            gamesRequestDTO.getPageNumber() - 1,
            gamesRequestDTO.getPageSize(),
            Sort.by(Sort.Direction.DESC, "id")
        ));
        GamesResponseDTO gamesResponseDTO = new GamesResponseDTO();

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

        List<GameStateParameterDTO> gameStateParameterDTOS = convertToGameStateParameterDTO(scheme.getGameStateParameters());

        SchemeDTO schemeDTO = convertToSchemeDTO(scheme);
        schemeDTO.setGameStateParameters(gameStateParameterDTOS);

        gameResponseDTO.setSchema(schemeDTO);
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

    private List<ExtractionPipeline> convertToExtractionPipeline(List<ExtractionPipelineDTO> pipelineDTO,Game game){
        List<ExtractionPipeline> listToReturn = new LinkedList<>();
        for(int i = 0; i<pipelineDTO.size(); i++){
            ExtractionPipeline pipeline = new ExtractionPipeline(pipelineDTO.get(i).getType(),
                    pipelineDTO.get(i).getInputFilename(),
                    pipelineDTO.get(i).getOutputFilename());
            if(pipelineDTO.get(i).getId() != 0){
                pipeline.setId(pipelineDTO.get(i).getId());
            }
            pipeline.setGame(game);
            listToReturn.add(pipeline);
        }
        return listToReturn;
    }

    private List<GameStateParameter> convertToGameStateParameter(List<GameStateParameterDTO> gameStateParameterDTOS, Scheme scheme){
        List<GameStateParameter> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateParameterDTOS.size(); i++){
            GameStateParameter gameStateParameter = new GameStateParameter(gameStateParameterDTOS.get(i).getKey(),gameStateParameterDTOS.get(i).getType(),
                    gameStateParameterDTOS.get(i).getLabel(),gameStateParameterDTOS.get(i).getDescription());
            if(gameStateParameterDTOS.get(i).getId() != 0){
                gameStateParameter.setId(gameStateParameterDTOS.get(i).getId());
            }
            gameStateParameter.setScheme(scheme);
            listToReturn.add(gameStateParameter);
        }
        return listToReturn;
    }

    private List<GameStateParameterDTO> convertToGameStateParameterDTO(List<GameStateParameter> gameStateParameters){
        List<GameStateParameterDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<gameStateParameters.size(); i++){
            GameStateParameterDTO gameStateParameterDTO = new GameStateParameterDTO(gameStateParameters.get(i).getKey(),gameStateParameters.get(i).getType(),
                    gameStateParameters.get(i).getLabel(),gameStateParameters.get(i).getDescription());
            gameStateParameterDTO.setId(gameStateParameters.get(i).getId());
            listToReturn.add(gameStateParameterDTO);
        }
        return listToReturn;
    }
    private Scheme convertToScheme(SchemeDTO schemeDTO){
        Scheme scheme = new Scheme(schemeDTO.getFilename());
        return scheme;
    }

    private SchemeDTO convertToSchemeDTO(Scheme scheme){
        SchemeDTO schemeDTO = new SchemeDTO(scheme.getFilename());
        return schemeDTO;
    }


    private List<PathDTO> convertToPathDTO(List<Path> paths){
        List<PathDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<paths.size(); i++){
            PathDTO pathDTO = new PathDTO(paths.get(i).getPath());
            pathDTO.setId(paths.get(i).getId());
            listToReturn.add(pathDTO);
        }
        return listToReturn;
    }
    private List<ExtractionPipelineDTO> convertToExtractionPipelineDTO(List<ExtractionPipeline> extractionPipelines){
        List<ExtractionPipelineDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<extractionPipelines.size(); i++){
            ExtractionPipelineDTO extractionPipelineDTO = new ExtractionPipelineDTO(
                    extractionPipelines.get(i).getType(),
                    extractionPipelines.get(i).getInputFilename(),
                    extractionPipelines.get(i).getOutputFilename());
            extractionPipelineDTO.setId(extractionPipelines.get(i).getId());
            listToReturn.add(extractionPipelineDTO);
        }
        return listToReturn;
    }
}


/*
{
        "name":"name999999",
        "description":"description1212121",
        "paths":[{
        "id":59,
        "path":"path99"
        }],
        "extractionPipeline": [{
        "id":84,
        "type":"sav-to-json99",
        "inputFilename":"i99",
        "outputFilename":"outpu121t"
        }],
        "schema": {
        "id":46,
        "filename":"filena3123me",
        "fields":[{
        "id":45,
        "key":"ke123y",
        "type":"ty124pe",
        "label":"la42bel",
        "description":"des124cription"
        }]
        }
        }
        */
