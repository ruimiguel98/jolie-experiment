package org.example.service;

import org.example.entity.Cart;
import org.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Annotation
@Service

// Class
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CartRepository productRepository;

    @Override
    public Cart saveProduct(Cart department) {
        return productRepository.save(department);
    }

    @Override
    public List<Cart> fetchProductList() {
        return (List<Cart>) productRepository.findAll();
    }

    @Override
    public Cart fetchProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Cart updateProduct(Cart product, Long productId) {
        Cart productDB = productRepository.findById(productId).get();

//        if (Objects.nonNull(product.getName())
//                && !"".equalsIgnoreCase(
//                product.getName())) {
//            productDB.setName(
//                    product.getName());
//        }
//
//        if (Objects.nonNull(
//                product.getDescription())
//                && !"".equalsIgnoreCase(
//                product.getDescription())) {
//            productDB.setDescription(
//                    product.getDescription());
//        }
//
//        if (Objects.nonNull(product.getPrice())
//                && !"".equalsIgnoreCase(
//                product.getPrice())) {
//            productDB.setPrice(
//                    product.getPrice());
//        }

        return productRepository.save(productDB);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}



