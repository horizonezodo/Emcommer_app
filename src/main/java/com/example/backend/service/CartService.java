package com.example.backend.service;

import com.example.backend.exception.ProductException;
import com.example.backend.model.Cart;
import com.example.backend.model.User;
import com.example.backend.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public com.example.backend.model.Cart findUserCart(Long userId);
}
