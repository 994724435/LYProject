package com.liaoyin.lyproject.common.aop.system;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.common.bean.SessionBean;
import com.liaoyin.lyproject.dao.TSystemUserOperationLogMapper;
import com.liaoyin.lyproject.entity.TSystemUserOperationLog;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间：2018/8/2 11:38
 * 描述：敏感接口验证
 */
@Aspect
@Component
@Configuration
public class SystemFilterAop {

    @Resource
    private TSystemUserOperationLogMapper logMapper;

    @Pointcut(value = "@annotation(com.liaoyin.lyproject.common.aop.system.SystemUserAnnotation)")
    public void access() {

    }

    @Before(value = "access()")
    public void deBefore(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String actionJoin = getControllerMethodDescription(joinPoint);
        SessionBean bean = Config.getSessionBeanToSystem(request);
        if (!Common.isNull(bean)) {
            TSystemUserOperationLog log = new TSystemUserOperationLog();
            log.setUseraccount(bean.getUserAccount());
            log.setRealname(bean.getUserNickName());
            log.setStatus(1);
            log.setRequestip(request.getRemoteAddr());
            log.setRequestthread(Thread.currentThread().getName());
            log.setCreatedate(new Date());
            log.setMessage(actionJoin);
            // 获取请求参数模块
            String classType = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(classType);
            String clazzName = clazz.getName();
            String methodName = joinPoint.getSignature().getName(); // 获取方法名称
            Object[] args = joinPoint.getArgs();// 参数
            // 获取参数名称和值
            Map<String, Object> nameAndArgs = getFieldsName(
                    this.getClass(), clazzName, methodName, args);
            log.setUserpassword(nameAndArgs.toString());
            logMapper.insertSelective(log);
        }
    }

    public static String getControllerMethodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemUserAnnotation.class)
                            .description();
                    break;
                }
            }
        }
        return description;
    }

    private Map<String, Object> getFieldsName(Class cls, String clazzName,
                                              String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        CodeAttribute codeAttribute = cm.getMethodInfo().getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);// paramNames即参数名
        }
        return map;
    }

}
