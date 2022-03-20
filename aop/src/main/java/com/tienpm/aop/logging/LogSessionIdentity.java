package com.tienpm.aop.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogSessionIdentity {
    private String parentSessionId;
    private String sessionId;
    private String flowId;
    private String userId;
    private String appId;
    private String method;
    private String uri;
}
