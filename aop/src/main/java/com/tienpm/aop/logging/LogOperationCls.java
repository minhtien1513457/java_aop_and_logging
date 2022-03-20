package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogOperationCls extends LogOperation{
    private String requestBody;
    private String responseBody;
}
