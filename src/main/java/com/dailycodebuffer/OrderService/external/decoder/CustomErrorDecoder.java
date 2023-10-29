package com.dailycodebuffer.OrderService.external.decoder;

import com.dailycodebuffer.OrderService.exception.CustomException;
import com.dailycodebuffer.OrderService.external.response.ProductErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}",response.request().url());
        log.info("::{}",response.request().headers());

        try {
            ProductErrorResponse productErrorResponse = objectMapper.readValue(response.body().asInputStream(),ProductErrorResponse.class);
            return new CustomException(productErrorResponse.getErrorMessage(),productErrorResponse.getErrorCode(),response.status());
        } catch (IOException e) {
            throw new CustomException("Internal server error","INTERNAL_SERVER_ERROR",500);
        }
    }
}
