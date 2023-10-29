package com.dailycodebuffer.OrderService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ORDER_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "PRODUCT_ID")
    private long productId;
    @Column(name = "PRODUCT_QUANTITY")
    private long quantity;
    @Column(name = "PRODUCT_ORDERED_DATE")
    private Instant orderDate;
    @Column(name = "PRODUCT_ORDER_STATUS")
    private String orderStatus;
    @Column(name = "PRODUCT_AMOUNT")
    private long amount;
}
