package org.example.controller;

import org.example.bean.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public User getUser(@RequestParam(name="id", required = true) UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping(value = "/create")
    public User createUser(@RequestBody User user) {
        user = userService.createUser(user);
        return user;
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user, @RequestParam("id") UUID userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam("id") UUID userId) {
        userService.deleteUserById(userId);
        return "User deleted successfully";
    }
}
