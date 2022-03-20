package com.tienpm.aop.webclient;

import com.tienpm.aop.config.Config;
import com.tienpm.aop.constants.OperationConstants;
import com.tienpm.aop.logging.LogOperationWebclient;
import com.tienpm.aop.logging.LogUtil;
import com.tienpm.aop.logging.LoggingExecutor;
import com.tienpm.aop.request.external.CreateProductRequest;
import com.tienpm.aop.response.external.CreateProductResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Supplier;

@Service
public class WebClientProductService {

    public CreateProductResponse createProduct(CreateProductRequest req) {
        String uri = UriComponentsBuilder
                .fromUriString(String.format("%s/products", Config.EXTERNAL_URL))
                .build().toUriString();

        HttpHeaders header = WebClientUtil.genBaseHeaders(null);
        LogOperationWebclient logOperationWebclient = LogUtil.
                createLogOperationForWebclientPost(OperationConstants.WebClientPaymentGatewayService.CREATE_PRODUCT, uri, header, req);
        Supplier<ResponseEntity> supplier = () -> WebClientUtil.post(uri, req, CreateProductRequest.class, header, CreateProductResponse.class);
        return LoggingExecutor.runAtWebClientOperation(supplier, logOperationWebclient);
    }
}
