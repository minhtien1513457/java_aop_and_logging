package com.tienpm.aop.logging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tienpm.aop.handler.HandledException;
import com.tienpm.aop.handler.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;
import java.util.function.Supplier;

public class LoggingExecutor {

    public static <T> T runAtController(Supplier<T> supplier, LogRequest logRequest) {
        LogContextHolder.startLogging(logRequest);
        LogResponse logResponse = null;
        try {
            T body = supplier.get();
            logResponse = LogUtil.createLogResponse(body);
            return body;
        } catch (HandledException he) {
            logResponse = LogUtil.createLogResponse(he);
            throw he;
        } catch (Exception e) {
            logResponse = LogUtil.createLogResponse(e);
            throw new InternalException();
        } finally {
            if (logResponse == null) {
                logResponse = LogUtil.createLogResponse(new InternalException());
            }
            LogContextHolder.endLogging(LogContextHolder.getSessionId(), logResponse);
        }
    }

    public static <T> T runAtOperation(Supplier<T> supplier, String action) {
        LogOperation logOperation = null;
        try {
            T result = supplier.get();
            logOperation = LogUtil.createLogOperation(action);
            return result;
        } catch (Exception ex) {
            logOperation = LogUtil.createLogOperation(action, ex);
            if (ex instanceof HandledException) {
                throw (HandledException) ex;
            } else {
                throw new InternalException(ex);
            }
        } finally {
            if (!Objects.isNull(LogContextHolder.getSessionId())) {
                LogContextHolder.putLogOperation(LogContextHolder.getSessionId(), logOperation);
            }else {
                Logger.info.info(new Gson().toJson(logOperation));
            }
        }
    }


    public static <T> T runAtWebClientOperation(Supplier<ResponseEntity> supplier, LogOperationWebclient logOperationWebclient) {
        try {
            ResponseEntity responseEntity = supplier.get();
            logOperationWebclient = LogUtil.createLogOperationForWebclient(logOperationWebclient, responseEntity);
            return (T) responseEntity.getBody();
        } catch (WebClientResponseException we) {
            logOperationWebclient = LogUtil.createLogOperationForWebclient(logOperationWebclient, we);
            JsonObject res = new Gson().fromJson(we.getResponseBodyAsString(), JsonObject.class);
            String message = res.get("message").getAsString() != null? res.get("message").getAsString() : we.getResponseBodyAsString();
            throw new HandledException(message, we.getRawStatusCode());
        } catch (Exception ex) {
            throw new InternalException(ex);
        } finally {
            if (!Objects.isNull(LogContextHolder.getSessionId())) {
                LogContextHolder.putLogOperation(LogContextHolder.getSessionId(), logOperationWebclient);
            }else {
                Logger.info.info(new Gson().toJson(logOperationWebclient));
            }
        }
    }
}
