package com.tienpm.aop.aspects;

import com.tienpm.aop.handler.HandledException;
import com.tienpm.aop.handler.InternalException;
import com.tienpm.aop.logging.LogContextHolder;
import com.tienpm.aop.logging.LogOperation;
import com.tienpm.aop.logging.LogResponse;
import com.tienpm.aop.logging.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(com.tienpm.aop.annotations.LogOperationLayer)")
    public void logOperationLayer() {
    }

    @Pointcut("@annotation(com.tienpm.aop.annotations.LogControllerLayer)")
    public void logControllerLayer() {
    }

    //AOP expression for which methods shall be intercepted
    @Around("(logOperationLayer())")
    public Object logOperation(ProceedingJoinPoint proceedingJoinPoint) {
        String action = this.getAction(proceedingJoinPoint);
        LogOperation logOperation = null;

        try {
            logOperation = LogUtil.createLogOperation(action);
            Object result = proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            logOperation = LogUtil.createLogOperation(action, ex);
            if (ex instanceof HandledException) {
                throw (HandledException) ex;
            } else {
                throw new InternalException(ex);
            }
        } finally {
            LogContextHolder.putLogOperation(LogContextHolder.getSessionId(), logOperation);
        }
    }

    @Around("(logControllerLayer())")
    public Object logController(ProceedingJoinPoint proceedingJoinPoint) {
        this.initialControllerLog(proceedingJoinPoint);
        LogResponse logResponse = null;

        try {
            Object result = proceedingJoinPoint.proceed();
            logResponse = LogUtil.createLogResponse(result);
            return result;
        } catch (HandledException he) {
            logResponse = LogUtil.createLogResponse(he);
            throw he;
        } catch (Throwable e) {
            logResponse = LogUtil.createLogResponse(e);
            throw new InternalException();
        } finally {
            if (logResponse == null) {
                logResponse = LogUtil.createLogResponse(new InternalException());
            }
            LogContextHolder.endLogging(LogContextHolder.getSessionId(), logResponse);
        }
    }

    private String getAction(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        return className + "." + methodName;
    }

    private void initialControllerLog(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, String> params = new HashMap<>();
        Object requestBody = null;
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof RequestBody) {
                    requestBody = args[argIndex];
                } else if (annotation instanceof RequestParam) {
                    RequestParam requestParam = (RequestParam) annotation;
                    params.put(requestParam.value(), Objects.isNull(args[argIndex]) ? "" : args[argIndex].toString());
                } else if (annotation instanceof PathVariable) {
                    PathVariable pathVariable = (PathVariable) annotation;
                    params.put(pathVariable.value(), Objects.isNull(args[argIndex]) ? "" : args[argIndex].toString());
                } else if (annotation instanceof RequestHeader) {
                    RequestHeader requestHeader = (RequestHeader) annotation;
                    params.put(requestHeader.value(), Objects.isNull(args[argIndex]) ? "" : args[argIndex].toString());
                }
            }
        }

        if (!Objects.isNull(requestBody) && !CollectionUtils.isEmpty(params)) {
            LogContextHolder.startLogging(LogUtil.createLogRequest(params, requestBody, this.getAction(proceedingJoinPoint)));
        } else if (!Objects.isNull(requestBody)) {
            LogContextHolder.startLogging(LogUtil.createLogRequest(requestBody, this.getAction(proceedingJoinPoint)));
        } else if (!CollectionUtils.isEmpty(params)) {
            LogContextHolder.startLogging(LogUtil.createLogRequest(params, this.getAction(proceedingJoinPoint)));
        } else {
            LogContextHolder.startLogging(LogUtil.createLogRequest(this.getAction(proceedingJoinPoint)));
        }
    }

}
