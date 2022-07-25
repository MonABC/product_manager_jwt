package com.example.product_manager.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    @ManyToMany
    @JoinTable(name = "user_role")
    private List<Role> roles;

    public User(String username, String password, String role, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.roles = roles;
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
