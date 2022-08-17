package org.example.controller;

import org.example.bean.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
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
    public Product getProduct(@RequestParam(name="id", required = true) int productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product, @RequestParam("id") int productId) {
        return productService.updateProduct(product, productId);
    }

    @DeleteMapping("/delete")
    public String deleteProduct(@RequestParam("id") int productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully";
    }
}
