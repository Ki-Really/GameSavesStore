package com.example.courseWork.repositories.gameRepositories;

import com.example.courseWork.models.gameModel.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Integer> {
}
