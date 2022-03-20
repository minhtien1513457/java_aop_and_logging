package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogOperation {
    private String name;
    private String status;
    private String errorMessage;
    private String stackTraceElements;
}
