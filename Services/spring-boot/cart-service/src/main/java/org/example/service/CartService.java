package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.bean.Cart;
import org.example.bean.CartProducts;
import org.example.bean.CartProductsId;
import org.example.repo.CartCRUD;
import org.example.repo.CartProductsCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private CartProducts cartProducts;

    @Autowired
    CartCRUD cartCRUD;

    @Autowired
    CartProductsCRUD cartProductsCRUD;

    @Value("${cart.topic.name}")
    private String cartTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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

    public String removeProductFromCart(CartProducts cartProducts) {
        CartProductsId cartProductsId = new CartProductsId();
        cartProductsId.setProductId(cartProducts.getProductId());
        cartProductsId.setCartId(cartProducts.getCartId());

        System.out.println("test " + cartProductsCRUD.findById(cartProductsId));

        cartProductsCRUD.deleteById(cartProductsId);

        return "Product " + cartProducts.getProductId() + " removed from cart " + cartProducts.getCartId();
    }

    public CartProducts addProductToCart(CartProducts cartProducts) {
        // send the productId as key and the quantity as the value to Product service
        kafkaTemplate.send(cartTopic, cartProducts.getProductId().toString(), cartProducts.getQuantity().toString());

        this.cartProducts = cartProducts;

        return cartProducts;
    }

    @KafkaListener(topics = "product-price-topic", groupId = "foo")
    public void listenGroupFoo(ConsumerRecord<String, String> consumer) {
        String productTotalPrice = consumer.value();
        cartProducts.setPriceTotal(Double.parseDouble(productTotalPrice));
        cartProductsCRUD.save(cartProducts);
    }
}
