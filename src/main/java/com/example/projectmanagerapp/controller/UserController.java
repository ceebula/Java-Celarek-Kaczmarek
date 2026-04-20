package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns user data for the given id")
    public Users getUserById(@Parameter(description = "User ID") @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user for the given id")
    public Users updateUser(@Parameter(description = "User ID") @PathVariable Long id, @RequestBody Users user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user for the given id")
    public void deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
    }
}