package org.example.service;

import org.example.bean.User;
import org.example.repo.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserCRUD userCRUD;

    public User createUser(User user) {
        user = userCRUD.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> products = (List<User>) userCRUD.findAll();
        return products;
    }

    public User getUserById(UUID userId) {
        return userCRUD.findById(userId).get();
    }

    public User updateUser(User user, UUID userId) {
        User userDB = userCRUD.findById(userId).get();

        if (Objects.nonNull(user.getRealName()) && !"".equalsIgnoreCase(user.getRealName())) {
            userDB.setRealName(user.getRealName());
        }

        if (Objects.nonNull(user.getPhone()) && !"".equalsIgnoreCase(user.getPhone())) {
            userDB.setPhone(user.getPhone());
        }

        if (Objects.nonNull(user.getAddress())) {
            userDB.setAddress(user.getAddress());
        }

        if (Objects.nonNull(user.getGender())) {
            userDB.setGender(user.getGender());
        }

        if (Objects.nonNull(user.getEmail())) {
            userDB.setEmail(user.getEmail());
        }

        return userCRUD.save(userDB);
    }

    public void deleteUserById(UUID userId) {
        userCRUD.deleteById(userId);
    }
}
