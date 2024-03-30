package com.example.courseWork.repositories.commonParameterRepositories;

import com.example.courseWork.models.commonParameters.CommonParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonParametersRepository extends JpaRepository<CommonParameter,Integer> {
}
