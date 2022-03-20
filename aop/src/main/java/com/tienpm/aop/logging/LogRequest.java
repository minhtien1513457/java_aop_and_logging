package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogRequest {
    private String action;
    private String params;
    private String body;
}
