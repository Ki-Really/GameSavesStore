package com.example.courseWork.repositories.gameRepositories;

import com.example.courseWork.models.gameModel.GameStateParameterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface GameStateParameterTypesRepository extends JpaRepository<GameStateParameterType,Integer> {
    Optional<GameStateParameterType> findByType(String type);
    Page<GameStateParameterType> findByTypeContaining(String type, Pageable pageable);
}
