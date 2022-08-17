package org.example.service;

import org.example.bean.User;
import org.example.repo.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserCRUD userCRUD;

    // ----------------------------------------------------------
    // --------------- Special operations
    // ----------------------------------------------------------
    public User addProductToCart(int userId, Long productId) {
        User userDB = userCRUD.findById(userId).get();

        if (Objects.nonNull(userDB.getCartProducts())) {

            List<Long> cartProducts = userDB.getCartProducts();
            cartProducts.add(productId);

            userDB.setCartProducts(cartProducts);
        }

        return userCRUD.save(userDB);
    }

    public User removeProductFromCart(int userId, Long productId) {
        User userDB = userCRUD.findById(userId).get();

        if (Objects.nonNull(userDB.getCartProducts())) {

            List<Long> cartProducts = userDB.getCartProducts();
            cartProducts.remove(productId);

            userDB.setCartProducts(cartProducts);
        }

        return userCRUD.save(userDB);
    }


    // ----------------------------------------------------------
    // --------------- CRUD operations
    // ----------------------------------------------------------
    public User createUser(User user) {
        user = userCRUD.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> products = (List<User>) userCRUD.findAll();
        return products;
    }

    public User getUserById(int userId) {
        return userCRUD.findById(userId).get();
    }

    public User updateUser(User user, int userId) {
        User userDB = userCRUD.findById(userId).get();

        if (Objects.nonNull(user.getName()) && !"".equalsIgnoreCase(user.getName())) {
            userDB.setName(user.getName());
        }

        if (Objects.nonNull(user.getPhone()) && !"".equalsIgnoreCase(user.getPhone())) {
            userDB.setPhone(user.getPhone());
        }

        if (Objects.nonNull(user.getAddress())) {
            userDB.setAddress(user.getAddress());
        }

        if (Objects.nonNull(user.getCartProducts())) {
            userDB.setCartProducts(user.getCartProducts());
        }

        return userCRUD.save(userDB);
    }

    public void deleteUserById(int userId) {
        userCRUD.deleteById(userId);
    }
}
