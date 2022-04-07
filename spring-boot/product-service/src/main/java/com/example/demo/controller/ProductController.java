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
    public Product saveProduct(@RequestBody Product product)
    {
        return productService.saveProduct(product);
    }

    // Read operation
    @GetMapping("/products")
    public List<Product> fetchDepartmentList()
    {
        return productService.fetchProductList();
    }

    // Update operation
    @PutMapping("/products/{id}")
    public Product
    updateDepartment(@RequestBody Product department,
                     @PathVariable("id") Long departmentId)
    {
        return productService.updateProduct(
                department, departmentId);
    }

    // Delete operation
    @DeleteMapping("/products/{id}")
    public String deleteDepartmentById(@PathVariable("id")
                                               Long productId)
    {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
