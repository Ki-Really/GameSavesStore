package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.gameModel.*;
import com.example.courseWork.repositories.gameRepositories.GamesRepository;
import io.minio.errors.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public void deleteById(int id) {
        gamesRepository.deleteById(id);
    }


    public GameResponseDTO constructGame(Game game){
        GameResponseDTO gameAddRequestDTO = new GameResponseDTO();
        gameAddRequestDTO.setId(game.getId());
        gameAddRequestDTO.setName(game.getName());
        gameAddRequestDTO.setDescription(game.getDescription());

        gameAddRequestDTO.setPaths(convertToPathDTO(game.getPaths()));
        gameAddRequestDTO.setExtractionPipeline(convertToExtractionPipelineDTO(game.getExtractionPipelines()));

        Scheme scheme = game.getScheme();

        List<FieldDTO> fieldsDTO = convertToFieldDTO(scheme.getFields());

        SchemeDTO schemeDTO = convertToSchemeDTO(scheme);
        schemeDTO.setFields(fieldsDTO);

        gameAddRequestDTO.setSchema(schemeDTO);
        gameAddRequestDTO.setImageId(game.getImage().getId());
        return gameAddRequestDTO;
    }
    @Transactional
    public void save(GameAddRequestDTO gameAddRequestDTO, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Game game = new Game(gameAddRequestDTO.getName(),gameAddRequestDTO.getDescription());
        List<Path> paths = convertToPath(gameAddRequestDTO.getPaths());

        for (Path path : paths) {
            path.setGame(game);
        }
        game.setPaths(paths);

        List<ExtractionPipeline> extractionPipelines= convertToExtractionPipeline(gameAddRequestDTO.getExtractionPipeline());
        for (ExtractionPipeline extractionPipeline : extractionPipelines) {
            extractionPipeline.setGame(game);
        }
        game.setExtractionPipelines(extractionPipelines);

        Scheme scheme = convertToScheme(gameAddRequestDTO.getSchema());
        scheme.setGame(game);

        List<Field> fields = convertToField(gameAddRequestDTO.getSchema().getFields());
        for (Field field : fields) {
            field.setScheme(scheme);
        }
        scheme.setFields(fields);
        game.setScheme(scheme);

        imagesService.upload(file,game);
        gamesRepository.save(game);
    }


    private List<Path> convertToPath(List<PathDTO> pathDTO){
        List<Path> listToReturn = new LinkedList<>();
        for(int i = 0; i<pathDTO.size(); i++){
            Path path = new Path(pathDTO.get(i).getPath());
            listToReturn.add(path);
        }
        return listToReturn;
    }

    private List<ExtractionPipeline> convertToExtractionPipeline(List<ExtractionPipelineDTO> pipelineDTO){
        List<ExtractionPipeline> listToReturn = new LinkedList<>();
        for(int i = 0; i<pipelineDTO.size(); i++){
            ExtractionPipeline pipeline = new ExtractionPipeline(pipelineDTO.get(i).getType(),
                    pipelineDTO.get(i).getInputFilename(),
                    pipelineDTO.get(i).getOutputFilename());
            listToReturn.add(pipeline);

        }
        return listToReturn;
    }

    private List<Field> convertToField(List<FieldDTO> fieldsDTO){
        List<Field> listToReturn = new LinkedList<>();
        for(int i = 0; i<fieldsDTO.size(); i++){
            Field field = new Field(fieldsDTO.get(i).getKey(),fieldsDTO.get(i).getType(),
                    fieldsDTO.get(i).getLabel(),fieldsDTO.get(i).getDescription());
            listToReturn.add(field);
        }
        return listToReturn;
    }

    private List<FieldDTO> convertToFieldDTO(List<Field> fields){
        List<FieldDTO> listToReturn = new LinkedList<>();
        for(int i = 0; i<fields.size(); i++){
            FieldDTO fieldDTO = new FieldDTO(fields.get(i).getKey(),fields.get(i).getType(),
                    fields.get(i).getLabel(),fields.get(i).getDescription());
            listToReturn.add(fieldDTO);
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
            listToReturn.add(extractionPipelineDTO);
        }
        return listToReturn;
    }
}