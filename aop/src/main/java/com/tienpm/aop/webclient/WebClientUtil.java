package com.tienpm.aop.webclient;

import com.tienpm.aop.constants.RestHeaderConstants;
import com.tienpm.aop.helper.ServiceRef;
import com.tienpm.aop.logging.LogContextHolder;
import com.tienpm.aop.logging.LogUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class WebClientUtil {
    public static String genFlowId() {
        String flowId = LogContextHolder.getFlowId();
        if (Objects.isNull(flowId)) {
            flowId = LogUtil.generateHttpRequestId();
        }
        return flowId;
    }

    public static HttpHeaders genBaseHeaders(HttpHeaders headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(RestHeaderConstants.HTTP_REQUEST_ID, WebClientUtil.genFlowId());
        if (!Objects.isNull(LogContextHolder.getSessionId())) {
            httpHeaders.add(RestHeaderConstants.HTTP_SESSION_ID, LogContextHolder.getSessionId());
        }
        if (!Objects.isNull(headers)) {
            httpHeaders.addAll(headers);
        }
        return httpHeaders;
    }

    public static <T, R> ResponseEntity post(String uri, T body, Class<T> classNameBody, HttpHeaders headers, Class<R> classNameResponse) {
        ResponseEntity responseEntity;
        if (Objects.isNull(body)) {
            responseEntity = ServiceRef.INSTANCE.webClient
                    .post()
                    .uri(uri)
                    .headers(httpHeaders -> {
                        httpHeaders.addAll(headers);
                    })
                    .retrieve()
                    .toEntity(classNameResponse)
                    .block();
        } else {
            responseEntity = ServiceRef.INSTANCE.webClient
                    .post()
                    .uri(uri)
                    .headers(httpHeaders -> {
                        httpHeaders.addAll(headers);
                    })
                    .body(Mono.just(body), classNameBody)
                    .retrieve()
                    .toEntity(classNameResponse)
                    .block();
        }
        return responseEntity;
    }
}
