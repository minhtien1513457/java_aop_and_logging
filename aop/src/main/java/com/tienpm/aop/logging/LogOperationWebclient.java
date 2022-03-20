package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogOperationWebclient extends LogOperation{
    private String url;
    private String method;
    private String request;
    private String header;
    private String responseBody;
    private int responseCode;
}
