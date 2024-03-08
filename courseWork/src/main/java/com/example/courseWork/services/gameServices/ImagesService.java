package com.example.courseWork.services.gameServices;

import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameModel.Image;
import com.example.courseWork.repositories.gameRepositories.ImageRepository;
import com.example.courseWork.services.props.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Transactional
public class ImagesService {
    private final ImageRepository imageRepository;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    @Autowired
    public ImagesService(ImageRepository imageRepository, MinioClient minioClient, MinioProperties minioProperties) {
        this.imageRepository = imageRepository;
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Transactional
    public void upload(MultipartFile file,Game game) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try{
            createBucket();
        }catch(Exception e){
            e.printStackTrace();
        }
        String fileName = generateFilename(file);
        InputStream inputStream;
        try{
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        save(inputStream,fileName);
    }

    private void createBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if(!found){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFilename(MultipartFile file){
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }
    private String getExtension(MultipartFile file){
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
    }

    private void save(InputStream inputStream, String fileName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream,inputStream.available(),-1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }


    /*@Transactional
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
    }*/

}
