package com.example.courseWork.services.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String pictureBucket;
    private String archiveBucket;
    private String url;
    private String accessKey;
    private String secretKey;


    public MinioProperties(String pictureBucket,String archiveBucket, String url, String accessKey, String secretKey) {
        this.pictureBucket = pictureBucket;
        this.archiveBucket = archiveBucket;
        this.url = url;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public MinioProperties() {
    }

    public String getPictureBucket() {
        return pictureBucket;
    }

    public void setPictureBucket(String pictureBucket) {
        this.pictureBucket = pictureBucket;
    }

    public String getArchiveBucket() {
        return archiveBucket;
    }

    public void setArchiveBucket(String archiveBucket) {
        this.archiveBucket = archiveBucket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
