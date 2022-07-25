package com.example.product_manager.service.user;

import com.example.product_manager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends UserDetailsService {
    User findByUserName(String username);

    User saveAdmin(User user);

    User saveUser(User user);

    Page<User> getByUsername(String q, Pageable pageable);

    Optional<User> findUserById(Long id);

    Page<User> findUserByName(String name, Pageable pageable);

    void removeById(Long id);
}
