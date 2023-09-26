package com.example.backend.controller;

import com.example.backend.exception.ProductException;
import com.example.backend.exception.UserException;
import com.example.backend.model.Review;
import com.example.backend.model.User;
import com.example.backend.request.ReviewRequest;
import com.example.backend.service.ReviewService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization")String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review=reviewService.createReview(req, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>>getProductsReview(@PathVariable Long productId){
        List<Review> reviews=reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews,HttpStatus.CREATED);
    }
}
