package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.repo.CartCRUD;
import org.example.repo.CartProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    CartCRUD cartCRUD;

    @Autowired
    CartProductsCRUD cartProductsCRUD;

    @Value("${cart.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    ObjectMapper om = new ObjectMapper();

    public Cart createCart(Cart cart) {

        UUID cartUUID = UUID.randomUUID();
        cart.setId(cartUUID);

        cartCRUD.save(cart);

        return cart;
    }

    public List<Cart> getAllCarts() {
        List<Cart> carts = (List<Cart>) cartCRUD.findAll();
        return carts;
    }

    public Cart getCartById(UUID cartId) {
        return cartCRUD.findById(cartId).get();
    }

    public Cart updateCart(Cart cart, UUID cartId) {
        Cart cartDB = cartCRUD.findById(cartId).get();

        if (Objects.nonNull(cart.getUserId())) {
            cartDB.setUserId(cart.getUserId());
        }

        return cartCRUD.save(cartDB);
    }

    public void deleteCartById(UUID cartId) {
        cartCRUD.deleteById(cartId);
    }

    public void addProductToCart(CartProducts cartProducts) {

        cartProducts.setPriceTotal(100.00);

        // release msg for product service
        String message = null;

        try {
            message = om.writeValueAsString(cartProducts.getProductId()); // pass the product ID to the product service
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topicName, message);


        cartProductsCRUD.save(cartProducts);
    }
}
