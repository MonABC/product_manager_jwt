package com.example.product_manager.service.role;

import com.example.product_manager.model.entity.Role;
import com.example.product_manager.repo.IRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService{
    @Autowired
    private IRoleRepo roleRepo;
    @Override
    public Iterable<Role> findAll() {
        return roleRepo.findAll();
    }
}
