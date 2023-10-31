package com.dailycodebuffer.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private String orderStatus;
    private Instant orderDate;
    private long amount;
    private ProductDetails productDetails;
    private PaymentResponse paymentResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetails {
        private String productName;
        private long id;
        private long price;
        private long quantity;
    }

}
