package com.example.courseWork.services;

import com.example.courseWork.models.Game;
import com.example.courseWork.models.Image;
import com.example.courseWork.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class ImagesService {
    private final ImageRepository imageRepository;
    @Autowired
    public ImagesService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    @Transactional
    public void save(MultipartFile file, Game game) throws IOException {
        Image image;
        if(file.getSize()!=0){
            image = toImageEntity(file);
            image.setGame(game);
            imageRepository.save(image);
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setBytes(file.getBytes());
        return image;
    }

}
