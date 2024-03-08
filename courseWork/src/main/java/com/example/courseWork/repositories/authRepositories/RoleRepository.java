package com.example.courseWork.repositories.authRepositories;

import com.example.courseWork.models.authModel.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
