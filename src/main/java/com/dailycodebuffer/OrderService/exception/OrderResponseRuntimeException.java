package com.dailycodebuffer.OrderService.exception;

import com.dailycodebuffer.OrderService.external.response.ProductErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderResponseRuntimeException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProductErrorResponse> handleProductServiceException(CustomException exception){
        return new ResponseEntity<ProductErrorResponse>(new ProductErrorResponse().builder().errorMessage(exception.getMessage()).errorCode(exception.getErrorCode()).build(), HttpStatus.valueOf(exception.getStatus()));
    }
}
