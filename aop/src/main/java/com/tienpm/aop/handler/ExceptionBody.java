package com.tienpm.aop.handler;

import lombok.Getter;

@Getter
class ExceptionBody {
    private final long timestamp;
    private final String message;


    private ExceptionBody(long timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }


    static ExceptionBody of(long timestamp, String message) {
        return new ExceptionBody(timestamp, message);
    }
}
