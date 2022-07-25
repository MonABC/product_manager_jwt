package com.example.product_manager.controller;

import com.example.product_manager.model.dto.CustomPage;
import com.example.product_manager.model.entity.User;
import com.example.product_manager.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<CustomPage> findALlUser(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        try {
            int pageCurrent = page >= 1 ? page - 1 : page;
            Pageable pageable = PageRequest.of(pageCurrent, size);
            Page<User> userPage = userService.getByUsername(q, pageable);
            if (userPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<User> users = userPage.getContent();
            Map<String, Object> paging = new HashMap<>();
            paging.put("currentPage", pageCurrent);
            paging.put("totalPage", userPage.getTotalElements());
            paging.put("totalElements", userPage.getTotalElements());
            paging.put("size", size);
            CustomPage customPage = new CustomPage();
            customPage.setContent(users);
            customPage.setPageable(paging);
            return new ResponseEntity<>(customPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> remove(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userService.findUserById(id);
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userService.removeById(id);
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserByName(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userService.findUserById(id);
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}

