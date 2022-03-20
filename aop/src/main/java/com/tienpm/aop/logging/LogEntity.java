package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogEntity {
    private long startAt;
    private String createdAt;
    private LogSessionIdentity sessionIdentity;
    private LogRequest request;
    private LogResponse response;
    private List<LogOperation> operations = new ArrayList<>();
}
