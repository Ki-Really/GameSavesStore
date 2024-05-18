package com.example.courseWork.repositories.gameRepositories;

import com.example.courseWork.models.gameModel.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamesRepository extends JpaRepository<Game,Integer> {
    Optional<Game> findByName(String name);
    Optional<Game> findById(int id);
    Page<Game> findByNameContainingOrDescriptionContaining(String name, String description,
                                                           Pageable pageable);
}
