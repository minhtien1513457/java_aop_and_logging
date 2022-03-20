package com.tienpm.aop.constants;

public interface OperationConstants {
    interface Status {
        String OK = "OK";
        String Failure = "Failure";
    }

    interface HttpMethod {
        String GET  = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

    interface WebClientPaymentGatewayService {
        String CREATE_PRODUCT  = "createProduct";
    }
}