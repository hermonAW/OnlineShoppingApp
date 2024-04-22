package com.marco.orderservice.service;

import com.marco.orderservice.client.InventoryClient;
import com.marco.orderservice.dto.OrderRequest;
import com.marco.orderservice.dto.OrderResponse;
import com.marco.orderservice.model.Order;
import com.marco.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderResponse placeOrder(OrderRequest orderRequest){
        //Map OrderRequest to Order object and save it to database

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        Order order = null;
        if(isProductInStock){
             order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();
            orderRepository.save(order);
            log.info("Order with order number {} saved to repository", order.getOrderNumber());
        } else {
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is not in stock");
        }

        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getSkuCode(), order.getPrice(), order.getQuantity());
    }
}
