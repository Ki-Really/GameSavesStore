package com.example.courseWork.services.gameServices;

import com.example.courseWork.models.gameModel.Path;
import com.example.courseWork.repositories.gameRepositories.PathsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PathsService {
    private final PathsRepository pathsRepository;
    @Autowired
    public PathsService(PathsRepository pathsRepository) {
        this.pathsRepository = pathsRepository;
    }
    @Transactional
    public void delete(Path path){
        pathsRepository.delete(path);
    }
}
