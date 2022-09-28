package org.example.controller;

import org.example.bean.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create")
    public Product createOrder(@RequestBody Product product) {
        product = productService.createProduct(product);
        return product;
    }

    @GetMapping("/all")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product")
    public Product getProduct(@RequestParam(name="id", required = true) UUID productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product, @RequestParam("id") UUID productId) {
        return productService.updateProduct(product, productId);
    }

    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam("id") UUID productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
