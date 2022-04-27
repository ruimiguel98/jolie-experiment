package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Annotation
@Service

// Class
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product department) {
        return productRepository.save(department);
    }

    @Override
    public List<Product> fetchProductList() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product fetchProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Product
    updateProduct(Product product, Long productId) {
        Product productDB = productRepository.findById(productId).get();

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



