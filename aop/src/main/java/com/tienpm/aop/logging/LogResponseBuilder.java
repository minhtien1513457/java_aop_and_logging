package com.tienpm.aop.logging;

public class LogResponseBuilder {
    private Integer code;
    private String body;
    private String errorMessage;
    private String stackTraceElements;

    public LogResponseBuilder code(Integer code) {
        this.code = code;
        return this;
    }

    public LogResponseBuilder body(String body) {
        this.body = body;
        return this;
    }

    public LogResponseBuilder errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public LogResponseBuilder stackTraceElements(String stackTraceElements) {
        this.stackTraceElements = stackTraceElements;
        return this;
    }

    public LogResponse build() {
        return new LogResponse(code, body, errorMessage, stackTraceElements);
    }
}