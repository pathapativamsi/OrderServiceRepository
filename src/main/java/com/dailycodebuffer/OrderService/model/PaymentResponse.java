package com.dailycodebuffer.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private long orderId;
    private long amount;
    private String referenceNumber;
    private String paymentMode;
}
