package com.example.courseWork.repositories;

import com.example.courseWork.models.Game;
import com.example.courseWork.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamesRepository extends JpaRepository<Game,Integer> {
    Optional<Game> findByName(String name);
    Optional<Game> findById(int id);
}
