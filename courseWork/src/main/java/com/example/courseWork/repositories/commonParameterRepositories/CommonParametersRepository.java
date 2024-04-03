package com.example.courseWork.repositories.commonParameterRepositories;

import com.example.courseWork.models.commonParameters.CommonParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonParametersRepository extends JpaRepository<CommonParameter,Integer> {
    Page<CommonParameter> findByLabelContainingOrDescriptionContaining(String label, String description, Pageable pageable);
}
