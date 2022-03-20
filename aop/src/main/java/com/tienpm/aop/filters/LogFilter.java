package com.tienpm.aop.filters;

import com.tienpm.aop.config.Config;
import com.tienpm.aop.constants.RestHeaderConstants;
import com.tienpm.aop.logging.LogContextHolder;
import com.tienpm.aop.logging.LogSessionIdentity;
import com.tienpm.aop.logging.LogUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String httpRequestId = request.getHeader(RestHeaderConstants.HTTP_REQUEST_ID);
        String httpSessionId = request.getHeader(RestHeaderConstants.HTTP_SESSION_ID);
        String userId = request.getHeader(RestHeaderConstants.USER_ID);

        if (Objects.isNull(httpRequestId)) {
            httpRequestId = LogUtil.generateHttpRequestId();
        }
        LogSessionIdentity logSessionIdentity = LogSessionIdentity.builder()
                .sessionId(httpSessionId)
                .flowId(httpRequestId)
                .method(request.getMethod())
                .appId(Config.APP_ID)
                .uri(request.getRequestURI())
                .userId(userId)
                .build();

        LogContextHolder.setLogSessionIdentity(httpRequestId,httpSessionId,logSessionIdentity);

        filterChain.doFilter(request, response);
    }


}
