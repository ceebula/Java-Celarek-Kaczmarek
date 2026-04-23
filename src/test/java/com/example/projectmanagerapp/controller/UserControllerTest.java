package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("Should return all users")
    void shouldReturnAllUsers() {
        Users user1 = new Users();
        user1.setUsername("User1");

        Users user2 = new Users();
        user2.setUsername("User2");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        List<Users> result = userController.getAllUsers();

        assertEquals(2, result.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by id")
    void shouldReturnUserById() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("User1");

        when(userService.getUserById(1L)).thenReturn(user);

        Users result = userController.getUserById(1L);

        assertEquals("User1", result.getUsername());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(userService.createUser(user)).thenReturn(user);

        Users result = userController.createUser(user);

        assertEquals("NewUser", result.getUsername());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        Users user = new Users();
        user.setUsername("UpdatedUser");

        when(userService.updateUser(1L, user)).thenReturn(user);

        Users result = userController.updateUser(1L, user);

        assertEquals("UpdatedUser", result.getUsername());
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        userController.deleteUser(1L);

        verify(userService, times(1)).deleteUser(1L);
    }
}