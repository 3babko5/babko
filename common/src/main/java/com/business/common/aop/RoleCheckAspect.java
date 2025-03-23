package com.business.common.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {

    @Around("@annotation(com.business.common.aop.RoleCheck)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드에서 RoleCheck 어노테이션 가져오기
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RoleCheck roleCheck = signature.getMethod().getAnnotation(RoleCheck.class);
        
        // 필요한 역할 목록
        String[] requiredRoles = roleCheck.roles();
        
        // 현재 요청에서 X-client-role 헤더 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientRole = request.getHeader("X-client-role");
        
        log.info("Required roles: {}, Client role: {}", Arrays.toString(requiredRoles), clientRole);
        
        // 역할 검증
        if (clientRole == null || !Arrays.asList(requiredRoles).contains(clientRole)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }
        
        // 권한 검증 통과, 메서드 실행
        return joinPoint.proceed();
    }
} 