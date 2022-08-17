package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.bean.Product;
import org.example.repo.ProductCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    ProductCRUD productCRUD;

    public Product createProduct(Product product) {
        product = productCRUD.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = (List<Product>) productCRUD.findAll();
        return products;
    }

    public Product getProductById(int productId) {
        return productCRUD.findById(productId).get();
    }

    public Product updateProduct(Product product, int productId) {
        Product productDB = productCRUD.findById(productId).get();

        if (Objects.nonNull(product.getName()) && !"".equalsIgnoreCase(product.getName())) {
            productDB.setName(product.getName());
        }

        if (Objects.nonNull(product.getDescription()) && !"".equalsIgnoreCase(product.getDescription())) {
            productDB.setDescription(product.getDescription());
        }

        if (Objects.nonNull(product.getType())) {
            productDB.setType(product.getType());
        }

        if (Objects.nonNull(product.getPrice())) {
            productDB.setPrice(product.getPrice());
        }

        return productCRUD.save(productDB);
    }

    public void deleteProductById(int productId) {
        productCRUD.deleteById(productId);
    }
}
