package com.example.courseWork.services.authServices;

import com.example.courseWork.models.authModel.Role;
import com.example.courseWork.repositories.authRepositories.RoleRepository;
import org.springframework.stereotype.Service;

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
