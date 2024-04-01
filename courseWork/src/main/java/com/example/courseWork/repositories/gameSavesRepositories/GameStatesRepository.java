package com.example.courseWork.repositories.gameSavesRepositories;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.models.sharedSave.SharedSave;
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
    Page<GameState> findAllBySharedSavesIn(List<SharedSave> sharedSaves, Pageable pageable);

}
