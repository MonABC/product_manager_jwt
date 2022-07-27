package com.example.product_manager.controller;

import com.example.product_manager.configuration.JwtProvider;
import com.example.product_manager.model.dto.LoginRequest;
import com.example.product_manager.model.dto.SignUpForm;
import com.example.product_manager.model.entity.User;
import com.example.product_manager.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                throw new Exception("body request valid");
            }
            User user = userService.findByUserName(request.getUsername());
            if (user == null) {
                throw new Exception(("user not exits"));
            }
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new Exception("password incorrect!");
            }
            String jwt = jwtProvider.generateTokenLogin(request.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", jwt);
            response.put("type", "Bearer");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignUpForm signUpForm) {
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User(signUpForm.getUsername(), signUpForm.getPassword(), signUpForm.getRole());
        if (signUpForm.getRole().equals("user")) {
            return
                    new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        }
        if (signUpForm.getRole().equals("admin")) {
            return new ResponseEntity<>(userService.saveAdmin(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/validated/username")
    public ResponseEntity<?> validateUsername(@RequestBody User user) {
        if (userService.findByUserName(user.getUsername()) != null) {
            User userFindByUsername = userService.findByUserName(user.getUsername());
            return new ResponseEntity<>(userFindByUsername, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        }
    }

}
