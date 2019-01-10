package com.liaoyin.lyproject.common.aop.user;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：
 * 时间：2018/8/2 11:38
 * 描述：敏感接口验证
 */
@Aspect
@Component
@Configuration
public class UserFilterAop {

    @Pointcut(value = "@annotation(com.liaoyin.lyproject.common.aop.user.UserAnnotation)")
    public void access() {

    }

    @Before(value = "access()")
    public void deBefore(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getResponse();

        }

}
