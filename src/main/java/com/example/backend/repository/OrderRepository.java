package com.example.backend.repository;

import com.example.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.id=:userId and (o.orderStatus='PLACED' or o.orderStatus='COnFIRMED' or o.orderStatus='SHIPPED' or o.orderStatus='DELIVERED')")
    public List<Order> getUsersOrders(@Param("userId")Long userId);
}
