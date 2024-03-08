package com.example.courseWork.services.gameSaveServices;

import com.example.courseWork.DTO.gameDTO.PathDTO;
import com.example.courseWork.DTO.gameSaveDTO.AddGameSaveDTO;
import com.example.courseWork.DTO.gameSaveDTO.MetadataDTO;
import com.example.courseWork.models.gameModel.*;
import com.example.courseWork.models.gameSaveModel.GameSave;
import com.example.courseWork.models.gameSaveModel.Metadata;
import com.example.courseWork.repositories.gameSavesRepositories.GameSavesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class GameSavesService {
    private final GameSavesRepository gameSavesRepository;
    @Autowired
    public GameSavesService(GameSavesRepository gameSavesRepository) {
        this.gameSavesRepository = gameSavesRepository;
    }
    public GameSave findByName(String name){
        return gameSavesRepository.findByName(name).orElse(null);
    }

    @Transactional
    public void save(AddGameSaveDTO addGameSaveDTO, MultipartFile file) {
        GameSave gameSave = new GameSave();
        gameSave.setName(addGameSaveDTO.getName());
        gameSave.setLocalPath(addGameSaveDTO.getLocalPath());
        List<MetadataDTO> metadataDTO = addGameSaveDTO.getMetadata();
        gameSave.setMetadataList(convertToMetadata(metadataDTO));
        gameSave.setSizeInBytes(file.getSize());

        gameSavesRepository.save(gameSave);
    }

    private List<Path> convertToPath(List<PathDTO> pathDTO){
        List<Path> listToReturn = new LinkedList<>();
        for(int i = 0; i<pathDTO.size(); i++){
            Path path = new Path(pathDTO.get(i).getPath());
            listToReturn.add(path);
        }
        return listToReturn;
    }

    private List<Metadata> convertToMetadata(List<MetadataDTO> metadataDTO){
        List<Metadata> listToReturn = new LinkedList<>();
        for(int i = 0;i<metadataDTO.size();i++){
            Metadata metadata = new Metadata(metadataDTO.get(i).getType(),
                    metadataDTO.get(i).getValue(),metadataDTO.get(i).getLabel(),
                    metadataDTO.get(i).getDescription());
            listToReturn.add(metadata);
        }
        return listToReturn;
    }
}
