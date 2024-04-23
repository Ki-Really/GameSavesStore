package com.example.courseWork.repositories.gameSavesRepositories;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.sharedSave.SharedSave;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameStatesRepository extends JpaRepository<GameState, Integer> {

    Optional<GameState> findByName(String name);
    Optional<GameState> findById(int id);
    void deleteById(int id);
    Page<GameState> findByPersonId(int id,Pageable pageable);
    Page<GameState> findByPersonIdAndNameContaining(int id,String name,Pageable pageable);
    Page<GameState> findByNameContainingAndIsPublicTrue(String name,Pageable pageable);

    Page<GameState> findByNameContainingAndGameIdAndGameNameContainingAndIsPublicTrue(
            String name,
            int gameId,
            String gameName,
            Pageable pageable
    );
    Page<GameState> findByNameContainingAndGameIdAndIsPublicTrue(
            String name,
            int gameId,
            Pageable pageable
    );
    Page<GameState> findByNameContainingAndGameNameContainingAndIsPublicTrue(String name,String gameName,Pageable pageable);

    Page<GameState> findByIsPublicTrue(Pageable pageable);
    Page<GameState> findByNameContaining(String name, Pageable pageable);
    Page<GameState> findByGameIdAndGameNameContainingAndIsPublicTrue(int gameId,String gameName,Pageable pageable);
    Page<GameState> findByGameIdAndIsPublicTrue(int gameId,Pageable pageable);
    Page<GameState> findByGameNameContainingAndIsPublicTrue(String gameName,Pageable pageable);
}
