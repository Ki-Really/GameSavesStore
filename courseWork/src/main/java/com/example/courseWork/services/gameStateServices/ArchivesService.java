package com.example.courseWork.services.gameStateServices;

import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameModel.Image;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.services.props.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class ArchivesService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    @Autowired
    public ArchivesService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }
    @Transactional
    public void upload(MultipartFile file,String name){
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
        putToS3(inputStream,name);
    }
    private void createBucket(){
        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProperties.getArchiveBucket())
                    .build());
            if(!found){
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProperties.getArchiveBucket())
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
                    .bucket(minioProperties.getArchiveBucket())
                    .object(fileName)
                    .build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getFileUrl(String name) {
        try {
            String bucketName = minioProperties.getArchiveBucket();

            String presignedUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .method(Method.GET)
                    .build());

            return presignedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while getting the file URL";
        }
    }

    public void removeFile(String archiveName) {
        try {
            String bucketName = minioProperties.getArchiveBucket();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(archiveName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while removing the file from Minio", e);
        }
    }

    @Transactional
    public void update(MultipartFile file, GameState gameState){
        try {
            removeFile(gameState.getArchiveName());
            if (file != null && !file.isEmpty()) {
                upload(file,gameState.getArchiveName());
                gameState.setUploadedAt(LocalDateTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
