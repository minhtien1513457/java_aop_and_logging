package com.tienpm.aop.logging;

import com.google.gson.Gson;
import com.tienpm.aop.constants.OperationConstants;
import com.tienpm.aop.handler.HandledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class LogUtil {
    private static final Gson gson = new Gson();

    public static LogRequest createLogRequest(String action) {
        return LogRequest.builder().action(action).build();
    }

    public static LogRequest createLogRequest(Map<String, String> params, String action) {
        return LogRequest.builder().action(action).params(gson.toJson(params)).build();
    }

    public static LogRequest createLogRequest(Object request, String action) {
        return LogRequest.builder().action(action).body(gson.toJson(request)).build();
    }

    public static LogRequest createLogRequest(Map<String, String> params, Object request, String action) {
        return LogRequest.builder().action(action).params(gson.toJson(params)).body(gson.toJson(request)).build();
    }

    public static LogResponse createLogResponse(HandledException e) {
        return LogResponse.builder()
                .code(e.getCode())
                .errorMessage(e.getMessage())
                .stackTraceElements(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                        .collect(Collectors.joining(System.lineSeparator())))
                .build();
    }

    public static LogResponse createLogResponse(Throwable e) {
        return LogResponse.builder()
                .code(500)
                .errorMessage(e.getMessage())
                .stackTraceElements(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                        .collect(Collectors.joining(System.lineSeparator())))
                .build();
    }

    public static LogResponse createLogResponse(Object body) {
        return LogResponse.builder().code(HttpStatus.OK.value()).body(gson.toJson(body)).build();
    }

    public static LogResponse createLogResponse() {
        return LogResponse.builder().code(HttpStatus.OK.value()).build();
    }

    public static LogOperation createLogOperation(String action) {
        return LogOperation.builder()
                .name(action)
                .status(OperationConstants.Status.OK)
                .build();
    }

    public static LogOperation createLogOperation(String action, Throwable e) {
        return LogOperation.builder()
                .name(action)
                .status(OperationConstants.Status.Failure)
                .errorMessage(e.getMessage())
                .stackTraceElements(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                        .collect(Collectors.joining(System.lineSeparator())))
                .build();
    }

    public static LogOperationWebclient createLogOperationForWebclientPost(String action, String url, Object header, Object request) {
        return LogOperationWebclient.builder()
                .name(action)
                .status(OperationConstants.Status.OK)
                .url(url)
                .method(OperationConstants.HttpMethod.POST)
                .request(Objects.isNull(request)?"":gson.toJson(request))
                .header(gson.toJson(header))
                .build();
    }

    public static String generateHttpRequestId() {
        return String.format("hri_%s", UUID.randomUUID().toString());
    }

    public static LogOperationWebclient createLogOperationForWebclient(LogOperationWebclient logOperationWebclient, ResponseEntity responseEntity) {
        logOperationWebclient.setStatus(OperationConstants.Status.OK);
        logOperationWebclient.setResponseCode(responseEntity.getStatusCode().value());
        logOperationWebclient.setResponseBody(new Gson().toJson(responseEntity.getBody()));
        return logOperationWebclient;
    }

    public static LogOperationWebclient createLogOperationForWebclient(LogOperationWebclient logOperationWebclient, WebClientResponseException we) {
        logOperationWebclient.setStatus(OperationConstants.Status.Failure);
        logOperationWebclient.setResponseCode(we.getRawStatusCode());
        logOperationWebclient.setResponseBody(we.getResponseBodyAsString());
        return logOperationWebclient;
    }
}
