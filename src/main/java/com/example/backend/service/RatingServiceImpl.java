package com.example.backend.service;

import com.example.backend.exception.ProductException;
import com.example.backend.model.Product;
import com.example.backend.model.Rating;
import com.example.backend.model.User;
import com.example.backend.repository.RatingRepository;
import com.example.backend.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService{

    private RatingRepository ratingRepo;
    private ProductService productService;

    public RatingServiceImpl(RatingRepository ratingRepo, ProductService productService) {
        this.ratingRepo = ratingRepo;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest re, User user) throws ProductException {
        Product product=productService.findProductById(re.getProductId());
        Rating rating=new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(re.getRating());
        rating.setCreatedAt(LocalDateTime.now());

        return ratingRepo.save(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepo.getAllProductsRating(productId);
    }
}
