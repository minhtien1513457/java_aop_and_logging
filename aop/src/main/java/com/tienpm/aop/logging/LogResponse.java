package com.tienpm.aop.logging;

public class LogResponse {
    private Integer code;
    private String body;
    private String errorMessage;
    private String stackTraceElements;
    private long elapsed;

    public LogResponse(Integer code, String body, String errorMessage, String stackTraceElements) {
        this.code = code;
        this.body = body;
        this.errorMessage = errorMessage;
        this.stackTraceElements = stackTraceElements;
    }

    public static LogResponseBuilder builder() {
        return new LogResponseBuilder();
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }
}
