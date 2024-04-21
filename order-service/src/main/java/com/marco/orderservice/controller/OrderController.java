package com.marco.orderservice.controller;

import com.marco.orderservice.dto.OrderRequest;
import com.marco.orderservice.dto.OrderResponse;
import com.marco.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest){
       return orderService.placeOrder(orderRequest);
        //return "Order placed successfully";
    }
}
