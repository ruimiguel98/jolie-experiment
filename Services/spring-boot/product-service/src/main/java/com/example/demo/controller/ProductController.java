package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Annotation
@RestController

// Class
public class ProductController {

    // Annotation
    @Autowired
    private ProductService productService;

    // Save operation
    @PostMapping("/products")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Read operation
    @GetMapping("/getProducts")
    public List<Product> fetchProductList() {
        return productService.fetchProductList();
    }

    // Read operation
    @GetMapping("/getProduct")
    public Product fetchProduct(@RequestParam(name="id", required = true) Long productId) {
        return productService.fetchProductById(productId);
    }

    // Update operation
    @PutMapping("/products/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable("id") Long productId) {
        return productService.updateProduct(product, productId);
    }

    // Delete operation
    @DeleteMapping("/products/{id}")
    public String deleteDepartmentById(@PathVariable("id") Long productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
