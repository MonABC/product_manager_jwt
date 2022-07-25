package com.example.product_manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
}
