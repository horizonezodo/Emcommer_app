package com.example.backend.repository;

import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT ci from CartItem ci where ci.cart=:cart and ci.product=:product and ci.size=:size and ci.userId=:userId")
    public CartItem isCartItemExists(@Param("cart")Cart cart,
                                     @Param("product")Product product,
                                     @Param("size")String size,
                                     @Param("userId")Long userId);
}
