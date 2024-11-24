package com.eunsil.bookmarky.global.config.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    ObjectMapper objectMapper;

    @Pointcut("execution(* com.eunsil.bookmarky..*Controller.*(..))")
    public void controller() {
    }

    @Around("controller()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            logRequestInfo(joinPoint, end - start);
        }
    }

    public void logRequestInfo(JoinPoint joinPoint, long timeMs) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "None";
        String controllerName = joinPoint.getSignature().getDeclaringTypeName();
        Map<String, Object> logInfo = new HashMap<>();

        try {
            String decodedRequestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
            logInfo.put("controller", controllerName);
            logInfo.put("http_method", request.getMethod());
            logInfo.put("request_uri", decodedRequestURI);
            logInfo.put("user", username);
            logInfo.put("execution_time", timeMs + "ms");
        } catch (Exception e) {
            log.error("LogAspect Error : {}", e.getMessage());
        } finally {
            printLog(logInfo);
        }
    }

    public void printLog(Map<String, Object> logInfo) {
        try {
            String jsonLog = objectMapper.writeValueAsString(logInfo);
            log.info(jsonLog);
        } catch (JsonProcessingException e) {
            log.error("Json Log Parsing Error : {}", e.getMessage());
        }
    }

}
