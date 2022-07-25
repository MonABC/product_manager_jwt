package com.example.product_manager.service.user;

import com.example.product_manager.model.entity.User;
import com.example.product_manager.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private IUserRepo userRepo;

    @Override
    public User findByUserName(String username) {
        return null;
    }

    @Override
    public User saveAdmin(User user) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public Page<User> getByUsername(String q, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Page<User> findUserByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public void removeById(Long id) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
