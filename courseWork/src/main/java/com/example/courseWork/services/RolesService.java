package com.example.courseWork.services;

import com.example.courseWork.models.Role;
import com.example.courseWork.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesService {
    private final RoleRepository roleRepository;

    public RolesService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role assignRole(String roleName){
        Role foundedRole = roleRepository.findByName(roleName);
        return foundedRole;
    }
}
