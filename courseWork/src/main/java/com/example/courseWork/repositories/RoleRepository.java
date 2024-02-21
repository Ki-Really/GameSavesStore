package com.example.courseWork.repositories;

import com.example.courseWork.models.Person;
import com.example.courseWork.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
