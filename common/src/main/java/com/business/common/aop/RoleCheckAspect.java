package com.business.common.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.application.exception.GlobalExceptionCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {

    @Around("@annotation(com.business.common.aop.RoleCheck)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RoleCheck roleCheck = signature.getMethod().getAnnotation(RoleCheck.class);

        // 필요한 역할 목록
        String[] requiredRoles = roleCheck.roles();

        // 현재 요청에서 X-client-role 헤더 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientRoleHeader = request.getHeader("X-client-role");

        if (clientRoleHeader == null || clientRoleHeader.isBlank()) {
            throw new BusinessLogicException(GlobalExceptionCode.FORBIDDEN);
        }

        // 헤더에서 콤마로 구분된 권한 분리
        String[] clientRoles = Arrays.stream(clientRoleHeader.split(","))
            .map(String::trim) // 공백 제거
            .toArray(String[]::new);

        log.info("Required roles: {}, Client roles: {}", Arrays.toString(requiredRoles), Arrays.toString(clientRoles));

        // 하나라도 포함되어 있으면 통과
        boolean hasRole = Arrays.stream(requiredRoles)
            .anyMatch(required -> Arrays.asList(clientRoles).contains(required));

        if (!hasRole) {
            throw new BusinessLogicException(GlobalExceptionCode.FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}
