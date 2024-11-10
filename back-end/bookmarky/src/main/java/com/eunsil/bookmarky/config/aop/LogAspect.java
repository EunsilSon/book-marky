package com.eunsil.bookmarky.config.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
            long timeMs = end - start;
            log.info("Execution Time: {} ms", timeMs);
        }
    }

    @Before("controller()")
    public void logBeforeExecution(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "None";
        String controllerName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> params = new HashMap<>();

        try {
            String decodedRequestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("http_method", request.getMethod());
            params.put("request_uri", decodedRequestURI);
            params.put("user", username);
        } catch (Exception e) {
            log.error("LogAspect Error", e);
        }

        log.info("Request - HTTP: {} | URI: {} | User: {} | Method: {}.{}",
                params.get("http_method"),
                params.get("request_uri"),
                params.get("user"),
                params.get("controller"),
                params.get("method"));
    }

}
