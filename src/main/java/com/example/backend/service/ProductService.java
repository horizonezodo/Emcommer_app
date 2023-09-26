package com.example.backend.service;

import com.example.backend.exception.ProductException;
import com.example.backend.model.Product;
import com.example.backend.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    public Product findProductById(Long productId) throws ProductException;
    public List<Product> findProductByCartegory(String category);
    public Page<Product> getAllProduct(String category, List<String>colors, List<String>sizes, double minPrice,double maxPrice, double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
    public List<Product> findAllProduct();
}
