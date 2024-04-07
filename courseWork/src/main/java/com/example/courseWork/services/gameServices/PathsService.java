package com.example.courseWork.services.gameServices;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gamePathDTO.GamePathDTO;
import com.example.courseWork.DTO.gamePathDTO.GamePathsRequestDTO;
import com.example.courseWork.models.gameModel.Path;
import com.example.courseWork.repositories.gameRepositories.PathsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PathsService {
    private final PathsRepository pathsRepository;
    private final ImagesService imagesService;

    @Autowired
    public PathsService(PathsRepository pathsRepository, ImagesService imagesService) {
        this.pathsRepository = pathsRepository;
        this.imagesService = imagesService;
    }
    /*public Path findById(int id){
        Optional<Path> path = pathsRepository.findById(id);
        return path.orElse(null);
    }
    public Page<Path> findByPath(String path, Pageable pageable){
        return pathsRepository.findByPathContaining(path,pageable);
    }
    public Page<Path> findAll(Pageable pageable){
        return pathsRepository.findAll(pageable);
    }*/
    public EntitiesResponseDTO<GamePathDTO> findPaths(GamePathsRequestDTO gamePathsRequestDTO){
        Page<Path> page;
        if(gamePathsRequestDTO.getSearchQuery()!=null && !gamePathsRequestDTO.getSearchQuery().isEmpty()){
            page = pathsRepository.findByPathContaining(gamePathsRequestDTO.getSearchQuery(), PageRequest.of(
                    gamePathsRequestDTO.getPageNumber() - 1,
                    gamePathsRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }else{
            page = pathsRepository.findAll(PageRequest.of(
                    gamePathsRequestDTO.getPageNumber() - 1,
                    gamePathsRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        EntitiesResponseDTO<GamePathDTO> gamePathsResponseDTO = new EntitiesResponseDTO<>();

        gamePathsResponseDTO.setItems(constructGamePath(page.getContent()));
        gamePathsResponseDTO.setTotalCount(page.getTotalElements());

        return gamePathsResponseDTO;
    }
    private List<GamePathDTO> constructGamePath(List<Path> paths){

        List<GamePathDTO> gamePaths = new LinkedList<>();
        for(Path path : paths){

            GamePathDTO gamePathDTO = new GamePathDTO();
            gamePathDTO.setGameId(path.getGame().getId());
            gamePathDTO.setGameName(path.getGame().getName());
            gamePathDTO.setGameIconUrl(imagesService.getFileUrl(path.getGame().getImage().getId()));
            gamePathDTO.setId(path.getId());
            gamePathDTO.setPath(path.getPath());

            gamePaths.add(gamePathDTO);
        }
        return gamePaths;
    }
}
