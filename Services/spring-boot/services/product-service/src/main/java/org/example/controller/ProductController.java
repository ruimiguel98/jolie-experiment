package org.example.controller;

import org.example.entity.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/getProducts")
    public List<Product> fetchProductList() {
        return productService.fetchProductList();
    }

    @GetMapping("/getProduct")
    public Product fetchProduct(@RequestParam(name="id", required = true) Long productId) {
        return productService.fetchProductById(productId);
    }

    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestBody Product product, @RequestParam("id") Long productId) {
        return productService.updateProduct(product, productId);
    }

    @DeleteMapping("/deleteProduct")
    public String deleteDepartmentById(@RequestParam("id") Long productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
