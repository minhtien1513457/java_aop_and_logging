package com.tienpm.aop.logging;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class LogContextHolder {
    private static final Gson gson = new Gson();
    public static String sessionPattern="%s_%s_%s";

    private static final ThreadLocal<LogSessionIdentity> logSessionIdentity = new ThreadLocal<LogSessionIdentity>();
    private static final Map<String, LogEntity> logContext = new HashMap<>();

    public static Long getUserId() {
        return Long.parseLong(logSessionIdentity.get().getUserId());
    }

    public static void setUserId(String userId) {
        logSessionIdentity.get().setUserId(userId);
    }

    public static void setLogSessionIdentity(LogSessionIdentity sessionIdentity) {
        logSessionIdentity.set(sessionIdentity);
    }

    public static void setLogSessionIdentity(String httpRequestId,String httpSessionId
            ,LogSessionIdentity sessionIdentity) {
        String sessionId;
        if (Objects.isNull(httpRequestId)) {
            httpRequestId = LogUtil.generateHttpRequestId();
            sessionId=String.format(sessionPattern,1,1, UUID.randomUUID());
        }
        else
        {
            if (Objects.isNull(httpSessionId)) {
                sessionId=String.format(sessionPattern,1,1, UUID.randomUUID());
            } else {
                List<String> sessions = Arrays.asList(httpSessionId.split("_"));
                Integer index = (Integer.valueOf(sessions.get(0)));
                sessionId=String.format(sessionPattern,index+1,1,UUID.randomUUID());
            }
        }
        sessionIdentity.setFlowId(httpRequestId);
        sessionIdentity.setSessionId(sessionId);
        logSessionIdentity.set(sessionIdentity);
    }

    public static LogSessionIdentity getLogSessionIdentity() {
        return logSessionIdentity.get();
    }
    public static String getSessionId() {
        if (!Objects.isNull(logSessionIdentity.get())) {
            return logSessionIdentity.get().getSessionId();
        }else {
            return null;
        }
    }

    public static String getFlowId() {
        if (!Objects.isNull(logSessionIdentity.get())) {
            return logSessionIdentity.get().getFlowId();
        }else {
            return null;
        }
    }

    public static void startLogging(LogRequest logRequest) {
        LogSessionIdentity sessionIdentity = logSessionIdentity.get();
        LogEntity logEntity = LogEntity.builder()
                .sessionIdentity(sessionIdentity)
                .startAt(System.currentTimeMillis())
                .createdAt(LocalDateTime.now().toString())
                .request(logRequest)
                .operations(new ArrayList<>())
                .build();
        logContext.put(sessionIdentity.getSessionId(), logEntity);
    }

    public static void endLogging(String sessionId, LogResponse logResponse) {
        LogEntity logEntity = logContext.get(sessionId);
        if (Objects.nonNull(logEntity)) {
            long start = logEntity.getStartAt() ;
            logResponse.setElapsed(System.currentTimeMillis() - start);
            logEntity.setResponse(logResponse);
            Logger.json.info(gson.toJson(logEntity));

            logContext.remove(sessionId);
            logSessionIdentity.remove();
        }
    }

    public static void putLogOperation(String sessionId, LogOperation logOperation) {
        LogEntity logEntity = logContext.get(sessionId);
        if (Objects.nonNull(logEntity)) {
            logEntity.getOperations().add(logOperation);
        }
    }

}
