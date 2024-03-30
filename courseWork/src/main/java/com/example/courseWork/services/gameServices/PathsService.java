package com.example.courseWork.services.gameServices;

import com.example.courseWork.models.gameModel.Path;
import com.example.courseWork.repositories.gameRepositories.PathsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PathsService {
    private final PathsRepository pathsRepository;

    public PathsService(PathsRepository pathsRepository) {
        this.pathsRepository = pathsRepository;
    }
    public Path findById(int id){
        Optional<Path> path = pathsRepository.findById(id);
        return path.orElse(null);
    }
}
