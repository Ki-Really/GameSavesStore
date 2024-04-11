package com.example.courseWork.repositories.graphicCommonRepositories;

import com.example.courseWork.models.graphicCommonModel.GraphicCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphicCommonsRepository extends JpaRepository<GraphicCommon,Integer> {

}
