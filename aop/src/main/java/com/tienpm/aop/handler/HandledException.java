package com.tienpm.aop.handler;

import lombok.Getter;

@Getter
public class HandledException extends RuntimeException {
    private final int code;

    public HandledException(String message, int code) {
        super(message);
        this.code = code;
    }

    HandledException(String message, int code, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
}
