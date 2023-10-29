package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrdersServiceImpl implements OrdersService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //save data to db
        //call product service to reduce quantity
        //payments service success or cancel
        log.info("reducing order quantity");
        productService.reduceProductQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("reduced order quantity");

        log.info("placing order request:{}",orderRequest);
        Order order = Order.builder()
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .amount(orderRequest.getTotalAmount())
                .build();
        orderRepository.save(order);

        log.info("order placed successfully order id:{}",order.getId());

        return order.getId();
    }
}
