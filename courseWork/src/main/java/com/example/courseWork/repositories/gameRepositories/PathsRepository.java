package com.example.courseWork.repositories.gameRepositories;

import com.example.courseWork.models.gameModel.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathsRepository extends JpaRepository<Path,Integer> {
    Page<Path> findByPathContaining(String path, Pageable pageable);
}
