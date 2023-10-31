package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.exception.CustomException;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.external.response.ProductErrorResponse;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.OrderResponse;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import com.dailycodebuffer.OrderService.model.ProductResponse;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrdersServiceImpl implements OrdersService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

        log.info("calling payment service");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderRequest.getProductId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment is successful, changing order status to successfull");
            orderStatus = "PAYMENT_SUCCESS";
        }
        catch (Exception e){
            log.info("Error in payment");
            orderStatus="PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("order placed successfully order id:{}",order.getId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("get order details for the order Id:{}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new CustomException("order with given id not found","ORDER_NOT_FOUND",404));

        log.info("invoking product service to fetch product details");

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(),ProductResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .id(productResponse.getId())
                .price(productResponse.getPrice())
                .productName(productResponse.getProductName())
                .quantity(productResponse.getQuantity())
                .build();

        return OrderResponse.builder()
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .productDetails(productDetails)
                .build();
    }
}
