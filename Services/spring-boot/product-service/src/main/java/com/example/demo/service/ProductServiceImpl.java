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

    // Save operation
    @Override
    public Product saveProduct(Product department) {
        return productRepository.save(department);
    }

    // Read operation
    @Override
    public List<Product> fetchProductList() {
        return (List<Product>)
                productRepository.findAll();
    }

    // Update operation
    @Override
    public Product
    updateProduct(Product department,
                  Long departmentId) {
        Product depDB
                = productRepository.findById(departmentId)
                .get();

        if (Objects.nonNull(department.getDepartmentName())
                && !"".equalsIgnoreCase(
                department.getDepartmentName())) {
            depDB.setDepartmentName(
                    department.getDepartmentName());
        }

        if (Objects.nonNull(
                department.getDepartmentAddress())
                && !"".equalsIgnoreCase(
                department.getDepartmentAddress())) {
            depDB.setDepartmentAddress(
                    department.getDepartmentAddress());
        }

        if (Objects.nonNull(department.getDepartmentCode())
                && !"".equalsIgnoreCase(
                department.getDepartmentCode())) {
            depDB.setDepartmentCode(
                    department.getDepartmentCode());
        }

        return productRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteProductById(Long departmentId) {
        productRepository.deleteById(departmentId);
    }
}



