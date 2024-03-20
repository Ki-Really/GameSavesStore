package com.example.courseWork.services.gameServices;

import com.example.courseWork.models.gameModel.ExtractionPipeline;
import com.example.courseWork.models.gameModel.Path;
import com.example.courseWork.repositories.gameRepositories.ExtractionPipelinesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class ExtractionPipelinesService {
    private final ExtractionPipelinesRepository extractionPipelinesRepository;
    @Autowired
    public ExtractionPipelinesService(ExtractionPipelinesRepository extractionPipelinesRepository) {
        this.extractionPipelinesRepository = extractionPipelinesRepository;
    }

    @Transactional
    public void delete(ExtractionPipeline extractionPipeline){
        extractionPipelinesRepository.delete(extractionPipeline);
    }

}
