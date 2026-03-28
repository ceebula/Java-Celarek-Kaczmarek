package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 Kontroler zarządzający użytkownikami systemu.
http://localhost:8080/api/users
 GET /api/users- Pobiera listę wszystkich użytkowników.
 POST /api/users - Tworzy nowego użytkownika.
 {
 "username": "jan_kowalski",
 "email": "jan@example.com",
 "password": "securePassword123"
 }
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET http://localhost:8080/api/users
    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // POST http://localhost:8080/api/users
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userRepository.save(user);
    }
}