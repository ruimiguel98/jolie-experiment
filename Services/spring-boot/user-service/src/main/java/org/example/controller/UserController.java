package org.example.controller;

import org.example.bean.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ----------------------------------------------------------
    // --------------- Special operations
    // ----------------------------------------------------------
    @PostMapping(value = "/cart/addProduct")
    public User addCartProduct(@RequestParam(name="userId", required = true) int userId,
                                 @RequestParam(name="productId", required = true) Long productId) {
        User user = userService.addProductToCart(userId, productId);
        return user;
    }

    @PostMapping(value = "/cart/removeProduct")
    public User removeCartProduct(@RequestParam(name="userId", required = true) int userId,
                                 @RequestParam(name="productId", required = true) Long productId) {
        User user = userService.removeProductFromCart(userId, productId);
        return user;
    }

    // ----------------------------------------------------------
    // --------------- CRUD operations
    // ----------------------------------------------------------
    @PostMapping(value = "/create")
    public User createUser(@RequestBody User user) {
        user = userService.createUser(user);
        return user;
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public User getUser(@RequestParam(name="id", required = true) int userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user, @RequestParam("id") int userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam("id") int userId) {
        userService.deleteUserById(userId);
        return "User deleted successfully";
    }
}
