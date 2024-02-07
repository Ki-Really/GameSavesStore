package com.example.courseWork.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdminStuff(){
        System.out.println("Only admins here!");
    }
}
