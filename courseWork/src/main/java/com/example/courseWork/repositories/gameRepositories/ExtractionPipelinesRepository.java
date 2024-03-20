package com.example.courseWork.repositories.gameRepositories;

import com.example.courseWork.models.gameModel.ExtractionPipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractionPipelinesRepository extends JpaRepository<ExtractionPipeline,Integer> {
}
