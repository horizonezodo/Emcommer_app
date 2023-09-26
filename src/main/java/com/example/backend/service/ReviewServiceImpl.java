package com.example.backend.service;

import com.example.backend.exception.ProductException;
import com.example.backend.model.Product;
import com.example.backend.model.Review;
import com.example.backend.model.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.ReviewRepository;
import com.example.backend.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    private ReviewRepository reviewRepo;
    private ProductService productService;
    private ProductRepository productRepo;

    public ReviewServiceImpl(ReviewRepository reviewRepo, ProductService productService,ProductRepository productRepo) {
        this.reviewRepo = reviewRepo;
        this.productService = productService;
        this.productRepo = productRepo;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {

        Product product=productService.findProductById(req.getProductId());
        Review review=new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepo.getAllProductsReview(productId);
    }
}
