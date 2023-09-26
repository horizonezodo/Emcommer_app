package com.example.backend.service;

import com.example.backend.model.OrderItem;
import com.example.backend.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    private OrderItemRepository orderRepo;

    public OrderItemServiceImpl(OrderItemRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderRepo.save(orderItem);
    }
}
