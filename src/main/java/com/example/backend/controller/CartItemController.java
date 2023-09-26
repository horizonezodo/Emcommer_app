package com.example.backend.controller;

import com.example.backend.exception.CartItemException;
import com.example.backend.exception.UserException;
import com.example.backend.model.CartItem;
import com.example.backend.model.User;
import com.example.backend.response.ApiResponse;
import com.example.backend.service.CartItemService;
import com.example.backend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                      @RequestHeader("Authorization")String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Item has been removed");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                   @PathVariable Long cartItemId,
                                                   @RequestHeader("Authorization")String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updateCartItem=cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        return new ResponseEntity<>(updateCartItem,HttpStatus.OK);
    }
}
