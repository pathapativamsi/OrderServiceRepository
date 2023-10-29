package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.model.OrderRequest;

public interface OrdersService {
    long placeOrder(OrderRequest orderRequest);
}
