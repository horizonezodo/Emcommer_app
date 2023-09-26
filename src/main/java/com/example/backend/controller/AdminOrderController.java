package com.example.backend.controller;

import com.example.backend.exception.OrderException;
import com.example.backend.model.Order;
import com.example.backend.response.ApiResponse;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandle(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> ConfirmedOrderHandle(@PathVariable("orderId") Long orderId,
                                                      @RequestHeader("Authorization")String jwt) throws OrderException{
        Order order=orderService.confirmedOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> ShippedOrderHandle(@PathVariable("orderId") Long orderId,
                                                      @RequestHeader("Authorization")String jwt) throws OrderException{
        Order order=orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> DeliveredOrderHandle(@PathVariable("orderId") Long orderId,
                                                      @RequestHeader("Authorization")String jwt) throws OrderException{
        Order order=orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> CancelledOrderHandle(@PathVariable("orderId") Long orderId,
                                                      @RequestHeader("Authorization")String jwt) throws OrderException{
        Order order=orderService.canceledOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse>DeleteOrderHandler(@PathVariable("orderId") Long orderId,
                                                         @RequestHeader("Authorization")String jwt)throws OrderException{
        orderService.deleteOrder(orderId);
        ApiResponse response=new ApiResponse();
        response.setMessage("Order deleted successfully");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
