package com.example.courseWork.services.authServices;

import com.example.courseWork.models.authModel.Role;
import com.example.courseWork.repositories.authRepositories.RoleRepository;
import com.example.courseWork.util.exceptions.personException.RoleNotExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesService {
    private final RoleRepository roleRepository;

    public RolesService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role assignRole(String roleName){
        Optional<Role> optionalFoundedRole = roleRepository.findByName(roleName);
        if(optionalFoundedRole.isPresent()){
            return optionalFoundedRole.get();
        }else{
            throw new RoleNotExistsException("Role " + roleName + " does not exists!");
        }
    }
}
