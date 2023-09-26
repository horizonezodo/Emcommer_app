package com.example.backend.service;

import com.example.backend.exception.OrderException;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepo;
    private CartService cartService;
    private AddressRepository addressRepo;
    private UserRepository userRepo;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRep;

    public OrderServiceImpl(OrderRepository orderRepo, CartService cartService, AddressRepository addressRepo, UserRepository userRepo, OrderItemService orderItemService, OrderItemRepository orderItemRep) {
        this.orderRepo = orderRepo;
        this.cartService = cartService;
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
        this.orderItemService = orderItemService;
        this.orderItemRep = orderItemRep;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address=addressRepo.save(shippingAddress);
        user.getAddress().add(address);
        userRepo.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems=new ArrayList<>();

        for (CartItem item: cart.getCartItems()){
            OrderItem orderItem=new OrderItem();

            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            OrderItem createdOrderItem=orderItemRep.save(orderItem);
            orderItems.add(createdOrderItem);

        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscounte(cart.getDiscounte());
        createdOrder.setTotalItem(cart.getTotalItem());

        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreateAt(LocalDateTime.now());

        Order saveOrder = orderRepo.save(createdOrder);

        for(OrderItem item:orderItems){
            item.setOrder(saveOrder);
            orderItemRep.save(item);
        }
        return saveOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt=orderRepo.findById(orderId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new OrderException("Could not find order with order id "+ orderId);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders = orderRepo.getUsersOrders(userId);
        return orders;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return orderRepo.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("COnFIRMED");
        return orderRepo.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepo.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepo.save(order);
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepo.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order=findOrderById(orderId);
        orderRepo.deleteById(orderId);
    }
}
