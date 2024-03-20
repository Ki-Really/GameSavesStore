package com.example.courseWork.services.gameServices;

import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameModel.Image;
import com.example.courseWork.repositories.gameRepositories.ImageRepository;
import com.example.courseWork.services.props.MinioProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
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

    public void removeFile(Image image) {
        try {

            String bucketName = minioProperties.getBucket();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(image.getName())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while removing the file from Minio", e);
        }
    }
    @Transactional
    public void save(Image image){
        imageRepository.save(image);
    }

    @Transactional
    public void update(MultipartFile file,Game game){
        try {
            // Удаляем старое изображение
            System.out.println("1");
            removeFile(game.getImage());
            System.out.println("2");
            // Загружаем новое изображение, если передан файл
            if (file != null && !file.isEmpty()) {
                upload(file,game.getImage().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete(Image image){
        imageRepository.delete(image);
    }

    @Transactional
    public void upload(MultipartFile file, String fileName){
        try{
            createBucket();
        }catch(Exception e){
            e.printStackTrace();
        }
        InputStream inputStream;
        try{
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        putToS3(inputStream,fileName);
    }

    private void createBucket(){
        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
            if(!found){
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void putToS3(InputStream inputStream, String fileName) {
       try{
           minioClient.putObject(PutObjectArgs.builder()
                   .stream(inputStream,inputStream.available(),-1)
                   .bucket(minioProperties.getBucket())
                   .object(fileName)
                   .build());
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public String getFileUrl(int id) {
        try {
            String bucketName = minioProperties.getBucket();
            Optional<Image> image = imageRepository.findById(id);

            String presignedUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(image.get().getName())
                    .method(Method.GET)
                    .build());

            return presignedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while getting the file URL";
        }
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
