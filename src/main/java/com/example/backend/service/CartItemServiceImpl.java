package com.example.backend.service;

import com.example.backend.exception.CartItemException;
import com.example.backend.exception.UserException;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{

    private CartItemRepository cartItemRepo;
    private UserService userService;
    private CartRepository cartRepo;

    public CartItemServiceImpl(CartItemRepository cartItemRepo, UserService userService, CartRepository cartRepo) {
        this.cartItemRepo = cartItemRepo;
        this.userService = userService;
        this.cartRepo = cartRepo;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        CartItem createdCartItem=cartItemRepo.save(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user=userService.findUserById(item.getUserId());
        if(user.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
        }

        return cartItemRepo.save(item);
    }


    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem=cartItemRepo.isCartItemExists(cart,product,size,userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem=findCartItemById(cartItemId);
        User user =userService.findUserById(cartItem.getUserId());
        User reqUser=userService.findUserById(userId);
        if(user.getId().equals(reqUser.getId())){
            cartItemRepo.deleteById(cartItemId);
        }
        else{
            throw new UserException("you can't remove another user item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {

        Optional<CartItem> opt=cartItemRepo.findById(cartItemId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new CartItemException("cart item is not found with id " + cartItemId);
    }
}
