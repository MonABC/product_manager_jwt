package com.example.product_manager.service.role;

import com.example.product_manager.model.entity.Role;

public interface IRoleService {
    Iterable<Role> findAll();
}
